import org.eclipse.paho.client.mqttv3.*;

class HerstellerMqttServer {

    private static MqttClient client;

    public static void main(String[] args) {
        String arg1 = "";
        if(args.length > 0)
        {
            arg1 = args[0];
        }
        try {
            client = new MqttClient("tcp://iot.eclipse.org:1883", "hersteller" + arg1);
            client.setCallback(new HerstellerMqttCallback());
            client.connect();
            client.subscribe("sh_sensor_data");

            WeatherDataMqtt.setTopic("sh_weather_data" + arg1);
            new Thread(new WeatherDataMqtt(client)).start();

        } catch (MqttException mex) {
            mex.getCause();
        }
    }

}
