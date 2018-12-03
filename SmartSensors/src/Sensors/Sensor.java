package Sensors;
import java.io.IOException;
import java.util.*;
import java.net.*;

 abstract public class Sensor implements Runnable{

     // For when the class is running
    protected boolean running;
    protected int sleepTime = 1000;
    protected String sensorName = "Sensor";

    //Destination Address
    protected InetAddress address;
    protected int port = 9876;

    //Variables needed to create a Package
    protected String message;
    protected byte[] buf;

    //For generation of random Data
    protected Random random = new Random();



    //Constructors
     public Sensor() throws UnknownHostException
    {
        this.address = InetAddress.getLocalHost();
        this.message = "testsensor: testdata";

    }
    public Sensor(InetAddress address, int port)
    {
        this.address = address;
        this.port = port;
        this.message = "testsensor: testdata";

    }

     public Sensor(InetAddress address)
     {
         this.address = address;
         this.message = "testsensor: testdata";
     }

     public void setSleepTime(int time)
     {
         this.sleepTime = time;
     }


    //Running this Code in a Thread
    public void run()
    {
        this.running = true;

        while(this.running)
        {
            try { Thread.sleep(sleepTime);
            } catch (InterruptedException ie) {ie.printStackTrace(); this.running = false; continue;}

            this.message = this.measure();

            try {sendMessage(this.message);
            } catch (IOException io) {io.printStackTrace();}

        }
    }

    protected String measure(){return null;} //Should be overridden

     //Sending Message with UDP
    protected void sendMessage(String message) throws IOException
    {
        this.buf = message.getBytes();
        DatagramPacket packet
                = new DatagramPacket(this.buf, this.buf.length, this.address, this.port);

        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
        socket.close();
        //sender.addPacket(packet);

        System.out.printf("%s:\t sending message to %s at Port %d\n", this.sensorName, this.address.getHostAddress(), this.port);
        //Insert UDP Send Code here
    }

     public static void main(String args[]) throws UnknownHostException, SocketException
     {
         BathSensor bath;
         HumiditySensor humidity;
         TempSensor temp;
         WindowSensor window;


         InetAddress address;
         int port;


         bath = new BathSensor();
         humidity = new HumiditySensor();
         temp = new TempSensor();
         window = new WindowSensor();

         Thread[] sensors = {new Thread(bath), new Thread(humidity), new Thread(temp), new Thread(window)};

         for(int i=0; i<sensors.length; i++)
         {
             sensors[i].start();
         }
     }
}
