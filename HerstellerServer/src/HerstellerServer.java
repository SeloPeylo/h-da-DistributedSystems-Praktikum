import org.eclipse.paho.client.mqttv3.*;

class HerstellerServer {


    public static void main(String[] args) throws MqttException {

        String port = "1884";
        if (args.length > 0) {
            port = args[0];
        }

        MqttClient client = new MqttClient(("tcp://localhost:" + port), MqttClient.generateClientId());
        client.setCallback(new SimpleMqttCallBack());
        client.connect();


    }

}
