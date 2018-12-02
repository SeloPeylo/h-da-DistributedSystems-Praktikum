package Sensors;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

/*
This class send each packet in its own thread and socket
 */
public class Sender implements Runnable{

    private Vector<DatagramPacket> packets = new Vector<>();
    private Map<Integer, DatagramSocket> sockets = new HashMap<>();

    public Sender() { }

    public void addPacket(DatagramPacket packet)
    {
        packets.add(packet);
    }

    public void run()
    {
        DatagramPacket packet;
        while(true)
        {
            try
            { Thread.sleep(100);
            } catch(InterruptedException ie){ie.printStackTrace();}
            if(packets.size() > 0)
            {
                try {
                    packet = packets.remove(0);
                    DatagramSocket socket;
                    if(sockets.get(packet.getPort()) == null)
                    {
                        socket = new DatagramSocket(packet.getPort());
                    } else { socket = sockets.get(packet.getPort()); }

                    socket.send(packet);
                    socket.close();
                } catch (SocketException se) {se.printStackTrace();}
                catch(IOException io) {io.printStackTrace();}
            }
        }
    }
}
