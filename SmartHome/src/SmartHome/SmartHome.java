package SmartHome;

import java.io.IOException;
import java.net.*;

public class SmartHome implements Runnable{

    private DatagramPacket packet = null;

    public SmartHome(DatagramPacket packet)
    {
        this.packet = packet;
    }

    public void run()
    {
        if(packet != null)
        {
            String message = new String(packet.getData());
            System.out.println("Message from "
                    + packet.getAddress()
                    + " Port "
                    + packet.getPort()
                    + " Message: " + message);
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
