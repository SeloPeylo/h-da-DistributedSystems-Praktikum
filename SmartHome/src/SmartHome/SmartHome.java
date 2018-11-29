package SmartHome;

import java.io.IOException;
import java.net.*;

public class SmartHome implements Runnable{


    private DatagramSocket socket;
    int port = 9876;
    InetAddress address;

    byte[] buf = new byte[256];
    DatagramPacket packet  = new DatagramPacket(buf, buf.length);
    private boolean isRunning = false;

    public SmartHome()
    {
        try
        {
            socket = new DatagramSocket(port);
        } catch (SocketException se) {se.printStackTrace();}
    }

    public void run()
    {
        System.out.println("Starting Smart-Home-Central");

        isRunning = true;
        while(isRunning)
        {
            buf = new byte[256];
            packet  = new DatagramPacket(buf, buf.length);
            try
            {
                socket.receive(packet);
                address = packet.getAddress();
                String message = new String(packet.getData());
                System.out.println(message);
            } catch (IOException io) {io.printStackTrace();}
        }
    }
}
