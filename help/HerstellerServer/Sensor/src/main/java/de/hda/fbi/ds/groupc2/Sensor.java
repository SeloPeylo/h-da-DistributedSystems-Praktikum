package de.hda.fbi.ds.groupc2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

import static java.lang.Thread.sleep;

public abstract class Sensor extends Thread{
    private DatagramSocket socket;
    private DatagramPacket packet;
    private InetAddress inetAddress;

    private int port = 9002;
    private String ip;
    private byte[] buffer = new byte[1024];

    public Sensor(String ip, String port) {
        this.port = Integer.parseInt(port);;
        this.ip = ip;
    }

    public Sensor(String ip) {
        this.port = 9002;
        this.ip = ip;
    }

    public Sensor() {
        this.port = port;
        this.ip = "localhost";
    }

    @Override
    public void run(){
        try {
            sendData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void receiveData(){

    }

    protected void sendData() throws IOException, InterruptedException {
        inetAddress = null;
        socket = null;
        socket = new DatagramSocket();

        while (true) {
            buffer = getSensorData().getBytes();
            packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), port);
            socket.send(packet);
            sleep(1000);
        }
    }

    protected int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public abstract String getSensorData();
}

