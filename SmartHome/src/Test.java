import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test implements Runnable {

    private static int receivedCount = 0;
    private static IMqttClient client;
    private MqttMessage message = null;

    public Test() {
        try {
            client = new MqttClient("tcp://iot.eclipse.org:1883", "centraltestt2");
            client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println(htmlTest());
        while (true) {
            try {

                Thread.sleep(1000);
                //SensorData.reduceDoubleSort();

                if (!client.isConnected()) {
                    client.connect();
                }
                byte[] payload = Integer.toString(receivedCount).getBytes();
                message = new MqttMessage(payload);
                client.publish("sh_test", message);

            } catch (MqttException mex) {
                mex.getCause();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String htmlTest(){
        String expectation = HttpServer.indexPage();
        String re = null;

        String urlToRead = "localhost:8080";
        StringBuilder result = new StringBuilder();
        URL url;
        try {
            url = new URL(urlToRead);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = null;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            re = result.toString();

            if (re.contains(expectation)) {
                re = "\n == HTML TEST == Test bestanden!\n";
            } else {
                re = "\n == HTML TEST == Test NICHT bestanden!\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    public static void setReceivedCount(int count) {
        receivedCount = count;
    }

    public static void publishMessage(MqttMessage message, String topic) {
        try {
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
