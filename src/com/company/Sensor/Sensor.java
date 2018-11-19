package com.company.Sensor;
import java.io.IOException;
import java.util.*;
import java.net.*;

 abstract public class Sensor implements Runnable{

     // For when the class is running
    protected boolean running;

    //Destination Address
    protected InetAddress address;
    protected int port = 9876;

    //Variables needed to create a Package
    protected String message;
    protected byte[] buf;
    protected DatagramSocket socket;

    //For generation of random Data
    protected Random random = new Random();



    //Constructors
     public Sensor()
    {
        try
        {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (SocketException se){se.printStackTrace();} catch (UnknownHostException uhe) {uhe.printStackTrace();}

        message
                = "testsensor: testdata"
                + " address: " + address.getHostAddress()
                + " port: " + port;
    }
    public Sensor(InetAddress address, int port) throws SocketException
    {
        try
        {
            this.socket = new DatagramSocket(port);
            this.address = InetAddress.getByName("localhost");
            this.port = port;
        } catch (SocketException se){se.printStackTrace();} catch (UnknownHostException uhe) {uhe.printStackTrace();}

        message
                = "testsensor: testdata"
                + " address: " + address.getHostAddress()
                + " port: " + port;
    }


    //Running this Code in a Thread
    public void run()
    {
        running = true;

        //this block is testing sending via UDP
       /* boolean test = false;
        try{test = testSending(message, address, port);} catch (IOException io){io.printStackTrace();}
        if(test == false)
        {
            System.out.println("Test failed: Cannot send and receive Data.");
            return;
        }
        */
        while(running)
        {
            try { Thread.sleep(1000);
            } catch (InterruptedException ie) {ie.printStackTrace(); running = false; continue;}

            message = this.measure();
            try {sendMessage(message, address, port);
            } catch (IOException io) {io.printStackTrace();}

        }
    }

    protected String measure(){return null;} //Should be overridden

     //Sending Message with UDP
    protected void sendMessage(String message, InetAddress address, int port) throws IOException
    {
        buf = message.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        //Insert UDP Send Code here
    }

    //Testing sending and receiving with udp socket
    protected boolean testSending(String message, InetAddress address, int port) throws IOException
    {
        buf = message.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(packet.getData());
        if(received.equals(message))
        {
            System.out.println("== SensorSendingTest == Message: " + received);
            return true;
        } else { return false; }
    }

}
