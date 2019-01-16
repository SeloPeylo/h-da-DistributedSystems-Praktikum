import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONException;
import org.json.JSONObject;

public class MqttPublisher implements Runnable {

    private String[] serverURL = null;

    public MqttPublisher(String[] serverURL) {
        for (int i = 0; i < serverURL.length; i++) {
            this.serverURL[i] = serverURL[i];
        }
    }

    @Override
    public void run() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setServerURIs(this.serverURL);
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
            client.connect(options);
            MqttMessage message = new MqttMessage();
            message.setPayload("Hello world from Java".getBytes());
            client.publish("iot_data", message);
            client.disconnect();
        } catch (MqttException mex) {
            mex.printStackTrace();
        }
    }
}
