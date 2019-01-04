package SmartHome;

import java.io.IOException;
import java.net.*;

public class SmartHome implements Runnable{

    private DatagramPacket packet = null;
    private int messagenr;
    private static int packagesReceived = 0;
    private SensorData sensorData;

    public SmartHome(DatagramPacket packet, int messagenr, SensorData sensorData)
    {
        this.packet = packet;
        this.messagenr = messagenr;
        this.sensorData = sensorData;
    }

    public void run()
    {
        if(packet != null)
        {
            String data = new String(packet.getData());
            String message = "Message #" + packagesReceived +
                    " from " + packet.getAddress() +
                    " Port " + packet.getPort() +
                    " Message: " + data;
            System.out.println(message);
            sensorData.addData(message);
        }
    }

    public static void main(String[] args) {

        // write your code here
        int port = 9876;
        SensorData sensorData = new SensorData();

        System.out.println("Starting Smart-Home-Central");
        for(int i=0; i<args.length; i++)
        {
            if (args[i].contains("port=")){
                String str = args[i];
                str.replace("port=", "");
                port = Integer.parseInt(str);
            }
        }
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(port);
        } catch(SocketException se) {se.printStackTrace(); System.exit(1);}

        while(true)
        {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try {
                socket.receive(packet);
                packagesReceived++;
                new Thread(new SmartHome(packet, packagesReceived, sensorData)).start();
            } catch (IOException io) {io.printStackTrace();}
        }
    }
}
