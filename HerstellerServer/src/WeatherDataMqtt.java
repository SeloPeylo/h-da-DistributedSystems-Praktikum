import org.eclipse.paho.client.mqttv3.*;

public class WeatherDataMqtt implements Runnable {

    private static IMqttClient client = null;
    private MqttMessage message = null;

    public WeatherDataMqtt(IMqttClient client) {
        this.client = client;
    }

    private void getWeather() {


        message = new MqttMessage();
    }

    @Override
    public void run() {
        try {
            if (!client.isConnected()) {
                client.connect();
            }
            while (client.isConnected()) {
                Thread.sleep(1000);
                getWeather();
                message.setRetained(true);
                message.setQos(0);
                client.publish("sh_weather_data", message);
                System.out.println("MQTT Message published!");
                //client.disconnect();
            }
        } catch (MqttException mex) {
            mex.getCause();
        } catch (InterruptedException iex) {
            iex.printStackTrace();
        }
    }
}
