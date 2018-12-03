package SmartHome;

import java.io.IOException;
import java.net.*;

public class SmartHome implements Runnable{

    private DatagramPacket packet = null;
    private static int packagesReceived = 0;

    public SmartHome(DatagramPacket packet)
    {
        this.packet = packet;
    }

    public void run()
    {
        if(packet != null)
        {
            packagesReceived++;
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
        System.out.println("Starting Smart-Home-Central");


        int port;
        if(args.length == 0)
        { port = 9876;
        } else { port = Integer.parseInt(args[0]); }

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
                new Thread(new SmartHome(packet)).start();
            } catch (IOException io) {io.printStackTrace();}
        }
    }
}
