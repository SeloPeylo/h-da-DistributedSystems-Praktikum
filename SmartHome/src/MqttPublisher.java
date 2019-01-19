import org.eclipse.paho.client.mqttv3.*;

import java.util.concurrent.Callable;

public class MqttPublisher implements Runnable{

    private IMqttClient client = null;
    private String clientID = "publisher1";
    private MqttMessage message = null;


    public MqttPublisher() {
        try {
            System.out.println("== Starting MQTT Client ==");
            client = new MqttClient("tcp://iot.eclipse.org:1883", clientID);
        } catch (MqttException mex) {
            mex.getCause();
        }


    }

    public void putMessage(byte[] payload) {
        message = new MqttMessage(payload);
    }

    @Override
    public void run() {
        try {
            client.connect();
            message.setRetained(true);
            message.setQos(0);
            client.publish("sensor_data", message);
            System.out.println("MQTT Message published!");
            client.disconnect();
        } catch (MqttException mex) {
            mex.getCause();
        }
    }
}
