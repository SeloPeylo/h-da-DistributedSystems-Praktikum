import org.eclipse.paho.client.mqttv3.*;

class HerstellerMqttServer {

    private static MqttClient client;

    public static void main(String[] args) {
        try {
            client = new MqttClient("tcp://iot.eclipse.org:1883", "hersteller1");
            client.setCallback(new HerstellerMqttCallback());
            client.connect();
            client.subscribe("sh_sensor_data");
            new Thread(new WeatherDataMqtt(client)).start();

        } catch (MqttException mex) {
            mex.getCause();
        }
    }

}
