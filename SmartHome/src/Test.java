import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Test implements Runnable {

    private static int receivedCount = 0;
    private IMqttClient client;
    private MqttMessage message = null;

    public Test() {
        try {
            client = new MqttClient("tcp://iot.eclipse.org:1883", "centraltest2");
            client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
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

    public static void setReceivedCount(int count) {
        receivedCount = count;
    }
}
