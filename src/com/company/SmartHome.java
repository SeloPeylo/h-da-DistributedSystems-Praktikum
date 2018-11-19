package com.company;

import java.io.IOException;
import java.net.*;

import com.company.Sensor.BathSensor;
import com.company.Sensor.HumiditySensor;
import com.company.Sensor.TempSensor;
import com.company.Sensor.WindowSensor;

public class SmartHome implements Runnable{

    private BathSensor bath = new BathSensor();
    private HumiditySensor humidity = new HumiditySensor();
    private TempSensor temp = new TempSensor();
    private WindowSensor window = new WindowSensor();

    private DatagramSocket socket;
    int port = 9876;
    InetAddress address;

    byte[] buf = new byte[256];
    DatagramPacket packet  = new DatagramPacket(buf, buf.length);
    private boolean isRunning = false;

    Thread[] sensors = {new Thread(bath), new Thread(humidity), new Thread(temp), new Thread(window)};

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
        for(int i=0; i<sensors.length; i++)
        {
            sensors[i].start();
        }
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
