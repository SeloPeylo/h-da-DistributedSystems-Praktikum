import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Test implements Runnable {

    private static int messageCount = 0;
    private static int arrivedCount = 0;
    private static IMqttClient client;
    private static String testResults = null;

    public Test() {
        try {
            client = new MqttClient("tcp://iot.eclipse.org:1883", "sensortest1");
            client.setCallback(new TestMqttCallback());
            client.connect();
            client.subscribe("sh_test");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                if (!client.isConnected()) {
                    client.connect();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MqttException ex) {
                ex.printStackTrace();
            }

        }
    }

    static String getTestResults() {
        messageCount = Sensor.getPackagesSend();
        float result = (float)arrivedCount / messageCount;
        result = (int)(result * 10000);
        result /= 100;
        testResults = "== TEST Results ==\n"
                + "\t - Messages sent " + messageCount
                + "\t - Messages received " + arrivedCount
                + "\t - Messages arrived % "
                + result
                + "\n== TEST Results ==";

        return testResults;
    }

    static void setArrivedCount(int count){
        arrivedCount = count;
    }
}