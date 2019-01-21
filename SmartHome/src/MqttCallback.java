import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

class MqttCallback implements org.eclipse.paho.client.mqttv3.MqttCallback {

    private static Vector<JSONObject> messages = new Vector<>();
    private String message = null;
    private JSONObject sensorData = null;

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) {
        message = new String(mqttMessage.getPayload());
        try {

            sensorData = new JSONObject(message);
            sensorData.put("topic", s);
        } catch (JSONException jex) {
            jex.getCause();
        }
        System.out.println("== MQTT == received: " + sensorData.toString() + " == MQTT ==");
        SensorData.addWeather(sensorData);
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}