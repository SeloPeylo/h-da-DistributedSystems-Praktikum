import Converter.Converter;
import Converter.MessageRoot;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;

public class MqttSubscriber {

    //The port of the protocol.
    private String brokerProtocol;

    // The address of the broker.
    //private String brokerAddress = "iot.eclipse.org";
    private String brokerAddress;
    //private String brokerAddress = "172.16.202.20";

    //The port of the broker.
    private String brokerPort;

    //The topic the MQTT client subscribes to.
    private String topic;

    private String brokerUrl;

    private MessageRoot data;

    public MqttSubscriber() {

        brokerUrl = brokerProtocol + "://" + brokerAddress + ":" + brokerPort;
    }

    public MqttSubscriber(String brokerProtocol, String brokerAddress, String brokerPort, String topic) {
        this.brokerProtocol = brokerProtocol;
        this.brokerAddress = brokerAddress;
        this.brokerPort = brokerPort;
        this.topic = topic;
        brokerUrl = brokerProtocol + "://" + brokerAddress + ":" + brokerPort;
    }

    public void run(){
        System.out.println("== START SUBSCRIBER ==");
        try {

            MqttClient client = new MqttClient(brokerUrl, MqttClient.generateClientId());

            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    data = Converter.fromJsonString(new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });


            client.connect();
            client.subscribe(topic);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public MessageRoot getData() {
        return data;
    }

    public String getBrokerProtocol() {
        return brokerProtocol;
    }

    public void setBrokerProtocol(String brokerProtocol) {
        this.brokerProtocol = brokerProtocol;
    }

    public String getBrokerAddress() {
        return brokerAddress;
    }

    public void setBrokerAddress(String brokerAddress) {
        this.brokerAddress = brokerAddress;
    }

    public String getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(String brokerPort) {
        this.brokerPort = brokerPort;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
