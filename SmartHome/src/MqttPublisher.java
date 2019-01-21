import org.eclipse.paho.client.mqttv3.*;

/*
 * This class publishes a Message requiring an active Connection
 */
public class MqttPublisher implements Runnable {

    private IMqttClient client;
    private MqttMessage message = null;
    private String topic = null;

    public MqttPublisher(IMqttClient client){
        this.client = client;
    }

    public void setMessage(MqttMessage message, String topic){
        this.message = message;
        this.topic = topic;
    }

    @Override
    public void run() {
        try {
            if (!client.isConnected()) {
                client.connect();
            }
            //message.setRetained(true);
            //message.setQos(0);
            client.publish(topic, message);
            System.out.println("== MQTT == published: " + message.toString() + " == MQTT ==");
        } catch (MqttException mex) {
            mex.getCause();
        }
    }
}
