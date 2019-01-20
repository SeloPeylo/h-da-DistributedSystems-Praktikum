package de.hda.fbi.ds.groupc2;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Subscriber implements MqttCallback {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Subscriber.class);
    /** The broker URL. */
    private String broker;
    protected String threadNumber;
    private String topic = "hda/fbi/ds/groupc2/weather";


    public Subscriber(String s) {
        this.threadNumber = s;
        broker = "tcp://iot.eclipse.org:1883";
    }

    /**
     * Runs the MQTT client.
     */
    public void read() {
        try {
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
            client.setCallback(this);

            // Connect to the MQTT broker.
            client.connect();
            LOGGER.info("Connected to MQTT broker: " + client.getServerURI());

            // Subscribe to a topic.
            client.subscribe(topic);
            LOGGER.info("Subscribed to topic: " + client.getTopic(topic));
        } catch (MqttException e) {
            LOGGER.error("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        LOGGER.error("Connection to MQTT broker lost!");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        LOGGER.info(threadNumber + ": " + new String(mqttMessage.getPayload()) );
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken mqttDeliveryToken) {
        try {
            LOGGER.info("Delivery completed: "+ mqttDeliveryToken.getMessage() );
        } catch (MqttException e) {
            LOGGER.error("Failed to get delivery token message: " + e.getMessage());
        }
    }
}