package Sensors;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/*
This class send each packet in its own thread and socket
 */
public class Sender implements Runnable{

    private DatagramPacket packet = null;
    private int portHost = 0;

    public Sender(DatagramPacket packet) throws SocketException
    {
        this.packet = packet;
    }
    public Sender(int portHost, DatagramPacket packet)
    {

        this.packet = packet;
    }

    public void run()
    {
        try
        {
            DatagramSocket socket;
            if(this.portHost != 0)
            {
                socket = new DatagramSocket(this.portHost);
            }
            else { socket = new DatagramSocket(); }

            socket.send(packet);
            socket.close();
        } catch (IOException io) {io.printStackTrace();}
    }
}
