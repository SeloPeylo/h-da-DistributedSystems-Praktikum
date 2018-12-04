package SmartHome;

import java.io.IOException;
import java.net.*;

public class SmartHome implements Runnable{

    private DatagramPacket packet = null;
    private int messagenr;
    private static int packagesReceived = 0;

    public SmartHome(DatagramPacket packet, int messagenr)
    {
        this.packet = packet;
        this.messagenr = messagenr;
    }

    public void run()
    {
        if(packet != null)
        {
            String message = new String(packet.getData());
            System.out.printf("Message #%d from %s Port %d Message: %s\n",
                    packagesReceived,
                    packet.getAddress(),
                    packet.getPort(),
                    message);
        }
    }

    public static void main(String[] args) {

        // write your code here
        int port = 9876;

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
                new Thread(new SmartHome(packet, packagesReceived)).start();
            } catch (IOException io) {io.printStackTrace();}
        }
    }
}
