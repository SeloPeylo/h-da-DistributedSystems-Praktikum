import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

class SimpleMqttCallBack implements MqttCallback {

    private String message = null;
    private JSONObject sensorData = null;

    public SimpleMqttCallBack()
    {

    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) {
        message = new String(mqttMessage.getPayload());
        try {

            sensorData = new JSONObject(message);
        } catch (JSONException jex) {
            jex.getCause();
        }
        System.out.println("Message received:\n\t" + sensorData.toString());
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
}