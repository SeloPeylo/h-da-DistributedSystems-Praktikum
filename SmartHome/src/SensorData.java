import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.Vector;


public class SensorData {

    private static Vector<JSONObject> dataList = new Vector<>();
    private static Vector<JSONObject> weatherList = new Vector<>();
    private static IMqttClient client = null;

    private static final String sensor_topic = "sh_sensor_data";
    private static final String weather_topic1 = "sh_weather_data1";
    private static final String weather_topic2 = "sh_weather_data2";
    private static final String weather_topic3 = "sh_weather_data3";

    public SensorData() {
        try {
            client = new MqttClient("tcp://iot.eclipse.org:1883", "smarthome1");
            client.setCallback(new MqttCallback());
            client.connect();
            client.subscribe(weather_topic1);
            client.subscribe(weather_topic2);
            client.subscribe(weather_topic3);
        } catch (MqttException mex) {
            mex.getCause();
        }
    }

    public void addData(JSONObject data) {
        Test.setReceivedCount(dataList.size());
        dataList.add(data);
        byte[] payload = String.valueOf(data).getBytes();
        MqttPublisher publisher = new MqttPublisher(client);
        publisher.setMessage(new MqttMessage(payload), sensor_topic);
        new Thread(publisher).start();
    }

    public static void addWeather(JSONObject weather) {
        weatherList.add(weather);
    }

    public Vector<JSONObject> getFilteredList(String key) {
        Vector<JSONObject> result = new Vector<>();

        if (dataList.size() <= 0) {
            return null;
        }

        for (int i = 0; i < dataList.size(); i++) {
            try {
                if (dataList.get(i).get("Sensorname").toString().contains(key)) {
                    result.add(dataList.get(i));
                }
            } catch (JSONException jex) {
                jex.printStackTrace();
            }
        }
        return result;
    }

    public Vector<JSONObject> getWeatherList() {
        Vector<JSONObject> result = new Vector<>();

        if (weatherList.size() <= 0) {
            return null;
        }

        for (int i = 0; i < weatherList.size(); i++) {
            result.add(weatherList.get(i));
        }
        return result;
    }

    public static void reduceDoubleSort() {
        try {

            for (int i = 0; i < dataList.size() - 1; i++) {
                int nr1 = Integer.parseInt(dataList.get(i).getString("Messagenr"));
                int nr2 = Integer.parseInt(dataList.get(i + 1).getString("Messagenr"));

                if (nr1 > nr2) {
                    dataList.insertElementAt(dataList.remove(i), i+1);
                    for (int j = i; j > 0; j--) {
                        nr1 = Integer.parseInt(dataList.get(j).getString("Messagenr"));
                        nr2 = Integer.parseInt(dataList.get(j - 1).getString("Messagenr"));
                        if(nr2 > nr1){
                            dataList.insertElementAt(dataList.remove(j), j-1);
                        }
                    }
                }
                if (nr1 == nr2) {
                    dataList.remove(i);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
