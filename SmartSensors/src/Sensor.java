import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/*
 * Diese Sensorklasse wird von den einzelnen Sensortypen geerbt und erweitert.
 * Sie stellt die Funktion zur Verfügung UDP packete über den Lokalhost zu versenden.
 */
abstract public class Sensor implements Runnable {

    protected boolean running;
    protected int sleepTime = 1000;             //Messintervall
    protected String sensorName = "Sensor";
    protected static int created = 0;
    protected int sensorNumber = created++;

    //Destination Address
    protected InetAddress address;
    protected int port = 9876;

    //Variables needed to create a Package
    protected String message;
    protected byte[] buf;
    protected static int packagesSend = 0;
    protected int messagenr;
    protected static DatagramSocket socket = null;

    //For generation of random Data
    protected final Random random = new Random();


    //Constructors
    public Sensor() throws UnknownHostException {
        this.address = InetAddress.getLocalHost();
        System.out.println("Localhost is " + this.address.getHostAddress());
        if (socket == null) {
            try {
                socket = new DatagramSocket(port + 1);
            } catch (SocketException se) {
                se.printStackTrace();
            }
        }
        this.message = "testsensor: testdata";

    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
        if (socket.getPort() != port) {
            try {
                socket.disconnect();
                socket.close();
                socket = new DatagramSocket(port + 1);
            } catch (SocketException se) {
                se.printStackTrace();
            }
        }
    }

    public void setSleepTime(int time) {
        this.sleepTime = time;
    }

    //Running this Code in a Thread
    public void run() {
        this.running = true;

        while (this.running) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                this.running = false;
                continue;
            }
            this.messagenr = packagesSend++;
            this.message = ""
                    + this.messagenr         //MessageNumber
                    + " "
                    + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) //Timestamp
                    + " "
                    + this.sensorName
                    + " "
                    + this.measure();    //Results of Sensor-Measurement

            try {
                sendMessage(this.message);
            } catch (IOException io) {
                io.printStackTrace();
            }

        }
    }

    protected String measure() {
        return null;
    } //Should be overridden

    //Sending Message with UDP
    protected void sendMessage(String message) throws IOException {
        this.buf = message.getBytes();
        DatagramPacket packet
                = new DatagramPacket(this.buf, this.buf.length, this.address, this.port);

        //DatagramSocket socket = new DatagramSocket();
        socket.send(packet);

        //socket.close();
        //sender.addPacket(packet);

        System.out.printf("== UDP == %s sending: %s == UDP ==\n",
                this.sensorName,
                this.message);
        //Insert UDP Send Code here
    }

    public static void main(String[] args) throws UnknownHostException {

        Vector<Sensor> sensors = new Vector<>();
        sensors.add(new BathSensor());
        sensors.add(new HumiditySensor());
        sensors.add(new WindowSensor());
        sensors.add(new TempSensor());

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-nftest")) {
                addTwentySensors(sensors);
                System.out.println("NotFunctional Test - adding 20 extra Sensors and SleepTime 5ms");
                for (Sensor k : sensors) {
                    k.setSleepTime(5);
                }
            }
            if (args[i].contains(".")) {
                InetAddress address = InetAddress.getByName(args[i]);
                for (Sensor k : sensors) {
                    k.setAddress(address);
                }
            }
            if (args[i].contains("waitms=")) {
                String str = args[i].replace("waitms=", "");
                int milliseconds = Integer.parseInt(str);
                for (Sensor k : sensors) {
                    k.setSleepTime(milliseconds);
                }
            }
            if (args[i].contains("port=")) {
                String str = args[i].replace("port=", "");
                int port = Integer.parseInt(str);
                for (Sensor k : sensors) {
                    k.setPort(port);
                }
            }
        }

        Vector<Thread> threads = new Vector<>();
        for (int i = 0; i < sensors.size(); i++) {
            threads.add(new Thread(sensors.get(i)));
        }
        for (Thread k : threads) {
            k.start();
        }
    }

    public static void addTwentySensors(Vector<Sensor> sensors) throws UnknownHostException {
        for (int i = 0; i < 5; i++) {

            sensors.add(new BathSensor());
            sensors.add(new HumiditySensor());
            sensors.add(new WindowSensor());
            sensors.add(new TempSensor());
        }
    }
}
