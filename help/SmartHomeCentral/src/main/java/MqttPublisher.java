import java.util.*;
import org.eclipse.paho.client.mqttv3.*;

public class MqttPublisher {
    //The port of the protocol.
    private String brokerProtocol;

    // The address of the broker.
    //private String brokerAddress = "iot.eclipse.org";
    //private String brokerAddress = "172.16.202.20";
    private String brokerAddress;

    //The port of the broker.
    private String brokerPort;

    //The topic the MQTT client subscribes to.
    private String topic;

    private String brokerUrl;

    private List<SenderInfo> senderInfoList;

    //message loss is not acceptable and your subscribers cannot handle duplicates
    private final int QOS_EXACTLY_ONCE = 2;

    /**
     * Default constructor with default broker url.
     * The server endpoint we’re using is a public MQTT broker hosted by the Paho project,
     * which allows anyone with an internet connection to test clients without the need of any authentication.
     */
    public MqttPublisher() {

        brokerUrl = brokerProtocol + "://" + brokerAddress + ":" + brokerPort;
        senderInfoList = new ArrayList<>();

    }

    /**
     * @param brokerProtocol
     * @param brokerAddress
     * @param brokerPort
     */
    public MqttPublisher(String brokerProtocol, String brokerAddress, String brokerPort, String topic) {
        this.brokerProtocol = brokerProtocol;
        this.brokerAddress = brokerAddress;
        this.brokerPort = brokerPort;
        brokerUrl = brokerProtocol + "://" + brokerAddress + ":" + brokerPort;
        this.topic = topic;
        senderInfoList = new ArrayList<>();
    }


    /**
     * we’re using the simplest constructor available,
     * which takes the endpoint address of our MQTT broker and a client identifier,
     * which uniquely identifies our client.
     *
     * We connect our MqttClient with the Server(Mqtt Broker) using connect function
     * MqttConnectionOptions ist optional, we can use it to allow us to customize some aspects of the protocol.
     *
     */
    public void run(){
        System.out.println("== START PUBLISHER ==");
        JsonConverter jsonConverter = new JsonConverter();
        this.setSenderInfoList(jsonConverter.getSensorList());

        String publisherId = UUID.randomUUID().toString();

        try {
            MqttClient client = new MqttClient(brokerUrl, publisherId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            //It will discard unsent messages from a previous run
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);

            for (int i = 0; i<this.getSenderInfoList().size(); i++){
                MqttMessage msg = readSensorInfos(i);
                msg.setQos(QOS_EXACTLY_ONCE);
                client.publish(topic,msg);
                System.out.println("Published message: " + msg);
            }

            // Disconnect from the MQTT broker.
            client.disconnect();
            System.out.println("Disconnected from MQTT broker.");

        } catch (MqttException e){
            e.getMessage();
        }
    }

    private MqttMessage readSensorInfos( int index) {

        SenderInfo sensor = this.getSenderInfoList().get(index);
        System.out.println(this.getSenderInfoList().size());

        byte[] payload = String.valueOf(sensor).getBytes();
        return new MqttMessage(payload);
    }

    public List<SenderInfo> getSenderInfoList() {
        return senderInfoList;
    }

    public void setSenderInfoList(List<SenderInfo> senderInfoList) {
        this.senderInfoList = senderInfoList;
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

    public String getBrokerProtocol() {
        return brokerProtocol;
    }

    public void setBrokerProtocol(String brokerProtocol) {
        this.brokerProtocol = brokerProtocol;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
