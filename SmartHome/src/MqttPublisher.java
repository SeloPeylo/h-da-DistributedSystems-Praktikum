import org.eclipse.paho.client.mqttv3.*;

public class MqttPublisher implements Runnable {

    private String[] serverURL = null;
    private MqttConnectOptions options = new MqttConnectOptions();
    private static byte[] payload = null;
    private static String clientID = MqttClient.generateClientId();
    private static int messagesSent = 0;


    public MqttPublisher(String[] serverURL) {
        if (serverURL.length > 0) {
            this.serverURL = new String[serverURL.length];
            for (int i = 0; i < serverURL.length; i++) {
                this.serverURL[i] = serverURL[i];
            }
            //options.setServerURIs(this.serverURL);

        }
        this.payload = payload;

    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    @Override
    public void run() {
        try {
            System.out.println("== Starting MQTT Client ==");

            MqttClient client = new MqttClient("tcp://localhost:1883", clientID);
            client.connect(options);
            MqttMessage message = new MqttMessage();
            message.setPayload(payload);
            message.setQos(2);
            client.publish("sensor_data", message);

            client.disconnect();


        } catch (MqttException mex) {
            mex.getCause();
        }


    }
}
