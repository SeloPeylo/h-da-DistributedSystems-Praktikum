package de.hda.fbi.ds.groupc2;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static java.lang.Thread.sleep;

public class Publisher extends Thread {
    //private static SmartHome smartHome = new SmartHome();
    private MqttConnectOptions mqttConnectOpts = new MqttConnectOptions();
    private String broker = "tcp://broker.hivemq.com:1883";
    private MemoryPersistence persistence = new MemoryPersistence();
    /** The message that is published. */
    private String msg;
    private Weather weather;

    Publisher(Weather s){
        this.weather = s;
    }

    /** The topic the MQTT client subscribes to. */
    private String topic = "hda/fbi/ds/groupc2/weather";

    public void run(){
        mqttConnectOpts.setCleanSession(true);

        try {
            while (true) {
                MqttClient client = new MqttClient(broker, MqttClient.generateClientId(), persistence);
                // Connect to the MQTT broker using the connection options.
                client.connect(mqttConnectOpts);
                System.out.println(client.getCurrentServerURI());

                // Create the message and set a quality-of-service parameter.
                MqttMessage message = new MqttMessage(weather.getCityName().getBytes());
                message.setQos(2);

                // Publish the message.
                client.publish(topic, message);

                // Disconnect from the MQTT broker.
                client.disconnect();
                sleep(1000);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
