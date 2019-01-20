import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SmartHome extends Thread {
    private int port = 9002;
    private DatagramSocket socket;

    private boolean isRunning;
    private String sentence;

    private String temperature;
    private String fan;
    private String window;
    private String humidity;

    @Override
    public void run() {
        isRunning = true;
        System.out.println("Server has started...");

        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        int tempCounter = 0;
        int fanCounter = 0;
        int windowCounter = 0;
        int humidityCounter = 0;

        while(isRunning) {
            Socket connectionSocket = null;
            byte[] buf = new byte[256];
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            DatagramSocket datagramSocket = null;
            try {
                datagramSocket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }

            //Sensordaten speichern
            File sensordata = new File("sensordata.csv");

            try {
                FileWriter writer = new FileWriter(sensordata.getAbsoluteFile(), true);

                socket.receive(packet);
                String tmp = new String(packet.getData(), 0, packet.getLength());

                if(tmp.startsWith("Temperature")){
                    temperature = tmp;
                    //temperatureList.add(tmp);
                    System.out.println(temperature + "  IP: " + packet.getAddress() + "  Port: " + packet.getPort());
                    tempCounter++;
                } else if (tmp.startsWith("Humidity")){
                    humidity = tmp;
                    System.out.println(humidity + "  IP: " + packet.getAddress() + "  Port: " + packet.getPort());
                    humidityCounter++;
                } else if (tmp.startsWith("RPM")){
                    fan = tmp;
                    System.out.println(fan + "  IP: " + packet.getAddress() + "  Port: " + packet.getPort());
                    fanCounter++;
                } else  if (tmp.startsWith("Fensterzustand")) {
                    window = tmp;
                    System.out.println(window + "  IP: " + packet.getAddress() + "  Port: " + packet.getPort() + "\n");
                    windowCounter++;
                }

                String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

                if ((tempCounter == 1) && (humidityCounter == 1) && (fanCounter == 1) && (windowCounter == 1) ) {
                    writer.write(timeStamp + ": \t" + temperature + " , " + humidity + " , " + window + " , " + fan + "\n");
                    tempCounter = 0;
                    humidityCounter = 0;
                    fanCounter = 0;
                    windowCounter = 0;
                }

                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTemp() {
        return temperature;
    }
    public String getFan() {
        return fan;
    }
    public String getWindow() { return window; }
    public String getHumidity() {
        return humidity;
    }
}
