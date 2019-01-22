import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * Callback um eine Nachricht zu bearbeiten
 */
class TestMqttCallback implements org.eclipse.paho.client.mqttv3.MqttCallback {

    private static String message = null;

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    /**
     * Empfängt von der Smart-Home-Zentrale wieviele Nachrichten angekommen sind
     * @param s topic
     * @param mqttMessage
     */
    public void messageArrived(String s, MqttMessage mqttMessage) {
        if(s.contains("sh_test1")){
            Test.setArrivedMessage(mqttMessage.toString());
        } else if(s.contains("sh_test")){
            Test.setArrivedCount(Integer.parseInt(mqttMessage.toString()));
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}