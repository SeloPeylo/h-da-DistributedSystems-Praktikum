import org.eclipse.paho.client.mqttv3.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class WeatherDataMqtt implements Runnable {

    private static IMqttClient client = null;
    private Vector<MqttMessage> messageList = new Vector<>();

    public WeatherDataMqtt(IMqttClient client) {
        this.client = client;
    }

    private void getWeather() {
        try {

            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Berlin&APPID=57c3088ea624194fea7ae6e3ea69dc8f");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            //System.out.println("Output from Server .... \n");


            while ((output = br.readLine()) != null) {

                MqttMessage message = new MqttMessage();
                message.setPayload(output.getBytes());
                messageList.add(message);
                System.out.println("\tMessage '"+ output);
            }

        } catch (Exception ex) {ex.printStackTrace();}
    }

    @Override
    public void run() {
        try {
            if (!client.isConnected()) {
                client.connect();
            }
            while (client.isConnected()) {
                getWeather();
                if(messageList.size() <= 0)
                {
                    continue;
                }
                Thread.sleep(1000);
                MqttMessage message = messageList.remove(0);
                message.setRetained(true);
                message.setQos(0);
                client.publish("sh_weather_data", message);
                System.out.println("MQTT published!");
                //client.disconnect();
            }
        } catch (MqttException mex) {
            mex.getCause();
        } catch (InterruptedException iex) {
            iex.printStackTrace();
        }
    }
}
