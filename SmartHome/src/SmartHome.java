

/*
 * The Smart Home receives the SensorData and saves the History in a Producer Consumer Pattern
 * It also has a HTML Webserver which is REST capable and where you can receive the data
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;

public class SmartHome implements Runnable {

    private DatagramPacket packet = null;
    private int messagenr;
    private static int packagesReceived = 0;
    private SensorData sensorData;

    public SmartHome(DatagramPacket packet, int messagenr, SensorData sensorData) {
        this.packet = packet;
        this.messagenr = messagenr;
        this.sensorData = sensorData;
    }

    public void run() {
        if (packet != null) {
            String receivedData = new String(packet.getData());
            String message = "== UDP == Message from " + packet.getAddress() +
                    " Port " + packet.getPort() +
                    " Message: " + receivedData +
                    " == UDP ==";
            System.out.println(message);
            String split[] = receivedData.split(" ");
            JSONObject data = new JSONObject();
            try {
                data.put("Address", packet.getAddress());
                data.put("Port", packet.getPort());
                data.put("Time", split[0]);
                data.put("Sensortype", split[1]);
                data.put("Message", (split[2] + " " + split[3]));
                data.put("Value", split[3]);
            } catch (JSONException jex) {
                jex.printStackTrace();
            }


            sensorData.addData(data);
        }
    }

    public static void main(String[] args) {

        // write your code here
        int listeningPort = 9876;

        System.out.println("Starting Smart-Home-Central");
        String[] serverURL = {"tcp://localhost:1884", "tcp://localhost:1885", "tcp://localhost:1886"};

        SensorData sensorData = new SensorData();
        HttpServer webServer = new HttpServer(sensorData);
        new Thread(webServer).start();

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("port=")) {
                String str = args[i];
                str.replace("port=", "");
                listeningPort = Integer.parseInt(str);
            }
        }
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(listeningPort);
            //socket.connect(InetAddress.getLocalHost(), listeningPort);
            //System.out.println("Socket Address: " + socket.getInetAddress().getHostAddress());
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        } /*catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }*/
        while (true) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try {
                socket.receive(packet);
                packagesReceived++;
                new Thread(new SmartHome(packet, packagesReceived, sensorData)).start();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }
}
