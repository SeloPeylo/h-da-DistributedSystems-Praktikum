import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 * Diese Sensorklasse wird von den einzelnen Sensortypen geerbt und erweitert.
 * Sie stellt die Funktion zur Verfügung UDP packete über den Lokalhost zu versenden.
 */
abstract public class Sensor implements Runnable {

    protected boolean running;      /** Ob die Run Methode läuft */
    protected int sleepTime = 1000; /** Intervall für die Messung/Nachricht */
    protected String sensorName = "Sensor"; /** Identifikation, um die Sensoren zu unterscheiden */
    protected static int sensorsCreated = 0; /** Zähler wieviele Sensoren es gibt */
    protected int sensorID = sensorsCreated++; /** SensorID Identifikation */

    //Destination Address
    protected InetAddress address;  /** Zieladresse der UDP Pakete */
    protected int port = 9876;  /** Zielport der UDP Pakete */

    //Variables needed to create a Package
    protected String message;   /** Nachricht die verschickt wird */
    protected byte[] buf;   /** Puffer für das DatagramPaket */
    protected static int packagesSend = 0; /** Zähler für die verschickten Packete */
    protected int messagenr;    /** Nummer der Nachricht */
    protected static DatagramSocket socket = null; /** DatagramSocket, hierüber werden die UDP Pakete geschickt */

    //For generation of random Data
    protected final Random random = new Random(); /** Zufallsgenerator für die Messdaten */


    /**
     * Der Konstruktor:
     * - setzt die Zieladresse auf Lokalhost
     * - erstellt einen neuen Datagram Socket, falls nicht vorhanden
     */
    public Sensor(){
        try {
            this.address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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

    /**
     *
     * @param address Zieladresse für die Pakete setzen
     */
    public void setAddress(InetAddress address) {
        this.address = address;
    }

    /**
     *
     * @param port Zielport setzen
     */
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

    /**
     *
     * @param time Intervall festlegen wie oft die Daten gesendet werden
     */
    public void setSleepTime(int time) {
        this.sleepTime = time;
    }

    //Running this Code in a Thread

    /**
     * Dieser Code wird von jedem Sensor in einem eigenen Thread ausgeführt:
     * - Schlafen nach sleepTime
     * - Zusammenstellung eines Packets
     * - Messdaten von measure()
     * - Senden mit sendMessage()
     */
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
            sendMessage(this.message);

        }
    }

    /**
     * Diese Funktion muss von den Unterklassen überschrieben werden.
     * @return
     */
    protected String measure() {
        return null;
    }

    /**
     * - Erstellt ein Datagram Packet
     * - Sendet die Nachricht über den Socket
     * - Gibt die gesendete Nachricht nochmal auf der Konsole aus
     * @param message
     */
    protected void sendMessage(String message) {
        this.buf = message.getBytes();
        DatagramPacket packet
                = new DatagramPacket(this.buf, this.buf.length, this.address, this.port);

        //DatagramSocket socket = new DatagramSocket();
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //socket.close();
        //sender.addPacket(packet);

        System.out.printf("== UDP == %s sending: %s == UDP ==\n",
                this.sensorName,
                this.message);
        //Insert UDP Send Code here
    }

    /**
     * Bekommt den Zähler wieviele Pakete geschickt wurden
     * @return
     */
    public static int getPackagesSend(){
        int result = packagesSend;
        return result;
    }

    /**
     * Die Main Funktion, welche die Sensoren erstellt und den Test je nach Argumenten einstellt:
     * - Setzt einen Shutdown-Hook in welcher die Testergebnisse dargestellt werden
     * - Erstellt 4 Sensoren verschiedenen Types
     * - Überprüft Programmargumente
     * - Setzt den test bei "-nftest"
     * - Ändert den Intervall bei "waitms="
     * - Ändert den Zielport bei "port="
     * - Erstellt für jeden Sensor einen Thread und startet ihn
     * @param args
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run()
            {
                System.out.println(Test.getTestResults());
            }
        });

        Vector<Sensor> sensors = new Vector<>();
        sensors.add(new BathSensor());
        sensors.add(new HumiditySensor());
        sensors.add(new WindowSensor());
        sensors.add(new TempSensor());

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-nftest")) {
                new Thread(new Test()).start();
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
            if (args[i].contains("-twenty")) {
                Test.addTwentySensors(sensors);
                for (Sensor k : sensors) {
                    k.setSleepTime(100);
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
}
