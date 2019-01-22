import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Vector;

class TestMqttCallback implements org.eclipse.paho.client.mqttv3.MqttCallback {

    private static String message = null;

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) {
        message = new String(mqttMessage.getPayload());
        Test.setArrivedCount(Integer.parseInt(message));
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}