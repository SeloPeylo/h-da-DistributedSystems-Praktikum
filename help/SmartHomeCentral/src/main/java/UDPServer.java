
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Sahil Nabizada.
 */
class UDPServer implements Runnable{

    private DatagramSocket serverSocket;
    private SenderInfo senderInfo;
    private int port;
    private boolean running = true;
    private List<SenderInfo> anfragen;
    private JSONArray sensors;

    /**
     *
     * @param port
     * @throws IOException
     */
    public UDPServer(int port) throws IOException{
        this.port = port;
        this.serverSocket = new DatagramSocket(this.port);
        anfragen = new ArrayList<>();
        sensors = new JSONArray();
    }

    @Override
    public void run(){
        try {
            while (running) {
                listen();
                reply();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creating a new server socket and binding it to a free port.
     * Receiving data and printing the received data
     * @throws IOException
     */
    private void listen() throws IOException{

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);

        String message = new String( receivePacket.getData()).trim();

        senderInfo = new SenderInfo(receivePacket.getAddress(), receivePacket.getPort(), message);

        addJSON(senderInfo);

        anfragen.add(senderInfo);
    }

    /**
     * Server replies to the client.
     * @throws IOException
     */
    private void reply() throws IOException{
        /**
         * Packet received from client will now be send it back to client
         * */
        DatagramPacket sendPacket = new DatagramPacket(senderInfo.getData().getBytes(),
                senderInfo.getData().getBytes().length, senderInfo.getIp(), senderInfo.getPort());
        serverSocket.send(sendPacket);
    }

    /**
     *
     * Stops the server.
     */
    public void stopServer() {
        this.running = false;
    }
    /**
     *
     * Start the server.
     */
    public void startServer() {
        this.running = true;
    }

    public boolean isRunning() {
        return running;
    }

    public List<SenderInfo> getAnfragen() {
        return anfragen;
    }

    public SenderInfo getSenderInfo() {
        return senderInfo;
    }

    /**
     * converts Sensors to the JSONObjects
     * @param sender
     */
    public void addJSON(SenderInfo sender){

        JSONObject sensorInfo = new JSONObject();

        sensorInfo.put("Ip", sender.getIp().toString());
        sensorInfo.put("Port", sender.getPort());
        sensorInfo.put("Data", sender.getData());

        JSONObject sensorObj = new JSONObject();

        if (sender.getData().substring(0,16).equals("Luftfeuchtigkeit")){
            sensorObj.put("Luftfeuchtigkeit", sensorInfo);
            sensors.add(sensorObj);
        }else if (sender.getData().substring(0,15).equals("LuefterDrehzahl")){
            sensorObj.put("LuefterDrehzahl", sensorInfo);
            sensors.add(sensorObj);
        }else if (sender.getData().substring(0,7).equals("Fenster")){
            sensorObj.put("Fenster", sensorInfo);
            sensors.add(sensorObj);
        }else if (sender.getData().substring(0,10).equals("Temparatur")){
            sensorObj.put("Temparatur", sensorInfo);
            sensors.add(sensorObj);
        }else {
            System.out.println("Not found.............!");
        }

        writeJSON();
    }

    /**
     * creates json file.
     * reads JSONArray(list of sensors)
     * writes JSONArray to the file
     */
    public void writeJSON(){
        URL resource = this.getClass().getResource("file1.json");

        System.out.println(resource);
        System.out.println(resource.getFile());

        try (FileWriter file = new FileWriter(resource.getFile())) {
            file.write(sensors.toJSONString());
            //System.out.println("Successfully Copied JSON Object to File...");
            //System.out.println("\nJSON Object: " + sensorObj);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
