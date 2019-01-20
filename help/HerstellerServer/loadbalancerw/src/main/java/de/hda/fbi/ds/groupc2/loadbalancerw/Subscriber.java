package de.hda.fbi.ds.groupc2.loadbalancerw;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public class Subscriber extends Thread implements MqttCallback {
    private static Connection con;
    private static Statement st;
    private static ResultSet rs;
    private static PreparedStatement ps;
    private static String msg;
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Subscriber.class);
    /** The broker URL. */
    private String broker;
    protected String threadNumber;
    private String message;
    private String topic = "hda/fbi/ds/groupc2";

    public static Connection DBConnect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/sensordaten","root", "");
            st = con.createStatement();
            // System.out.println("Connected: ");
            return con;
        }catch (Exception ex){
            System.out.println("Error: " + ex);
        }
        return null;
    }

    public static ArrayList<String> getData() throws Exception {
        String sensor;
        String serverName;
        String ausgabeNachricht;
        try {
            con = DBConnect();
            ps = con.prepareStatement("SELECT * From daten WHERE ID > 0 ORDER BY ID DESC LIMIT 10");
            rs = ps.executeQuery();
            ArrayList<String> array = new ArrayList<String>();
            System.out.println("The Last 10 Records are selected! ");
            while (rs.next()){
                sensor = rs.getString("Sensor");
                serverName = rs.getString("ServerName");
                ausgabeNachricht = sensor + " \t von " + serverName;

                array.add(ausgabeNachricht);
            }

            for (int i=array.size();i>0;i--){
                System.out.println("--> "+ array.get(i-1));
            }

            return array;
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
        return null;
    }

    public void setData(String setMsg){
        String serverName = "Server1";
        try {
            con = DBConnect();
            ps = con.prepareStatement("INSERT INTO daten(Sensor,ServerName) VALUES ('"+setMsg+"','"+serverName+"')");
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println("uppss: " + e);
        }
    }



    public Subscriber(String s) {
        this.threadNumber = s;
        broker = "tcp://iot.eclipse.org:1883";
    }


    /**
     * Runs the MQTT client.
     */
    public void run() {
        try {
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
            client.setCallback(this);

            // Connect to the MQTT broker.
            client.connect();
            LOGGER.info("Connected to MQTT broker: " + client.getServerURI());

            // Subscribe to a topic.
            client.subscribe(topic);
            LOGGER.info("Subscribed to topic: " + client.getTopic(topic));
            getData();
        } catch (MqttException e) {
            LOGGER.error("An error occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        LOGGER.error("Connection to MQTT broker lost!");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        LOGGER.info(threadNumber + ": " + new String(mqttMessage.getPayload()) );
        message = new String(mqttMessage.getPayload());
        setData(message);
    }


    public String messageA(String s, MqttMessage mqttMessage) throws Exception {
        return new String(mqttMessage.getPayload());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken mqttDeliveryToken) {
        try {
            LOGGER.info("Delivery completed: "+ mqttDeliveryToken.getMessage() );
        } catch (MqttException e) {
            LOGGER.error("Failed to get delivery token message: " + e.getMessage());
        }
    }

    public String getMessage() {
        return message;
    }
}