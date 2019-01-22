import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Vector;

/**
 * Testklasse welche parallel läuft und testet zur Laufzeit
 * Getestet wird durch einen Vergleich der Anzahl geschickter UDP Packete und der Anzahl tatsächtlich angekommener Packete.
 * Die Angekommenen Pakete zählt die SmartHomeZentrale und schickt dieser Klasse per Mqtt zu.
 */
public class Test implements Runnable {

    private static int messageCount = 0; /** Zählt wieviele Nachrichten gesendet wurden */
    private static int arrivedCount = 0; /** Anzahl tatsächtlich angekommener Nachrichten */
    private static int errorCount = 0;
    private static IMqttClient client;
    private static String testResults = null; /** Auusgabe der Testergebnisse */
    private static String arrivedMessage = "reset";

    /**
     * Konstruktor:
     * - erstellt neuen MqttClienten und verbindet ihn
     * - Topic sh_test wird subscribed
     * - Setzt einen Calllback um Mqtt Nachrichten zu verarbeiten!
     */
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

    /**
     * Erstellt zwanzig weitere Sensoren
     * @param sensors
     */
    public static void addTwentySensors(Vector<Sensor> sensors)  {
        for (int i = 0; i < 5; i++) {
            sensors.add(new BathSensor());
            sensors.add(new HumiditySensor());
            sensors.add(new WindowSensor());
            sensors.add(new TempSensor());
        }
    }

    /**
     * Thread läuft alle 1000ms
     * Hält die Verbindung nur am Leben
     */
    @Override
    public void run() {
        while (true) {
            try {
                if (!client.isConnected()) {
                    client.connect();
                }
                client.publish("sh_test1", new MqttMessage("sh_test1".getBytes()));
                arrivedMessage = "reset";
                Thread.sleep(1000);

                if(!arrivedMessage.contains("sh_test1")){
                    errorCount++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MqttException ex) {
                ex.printStackTrace();
            }

        }
    }

    /**
     * Berechnet die Testergebnisse und gibt sie wieder:
     * - Angekommene Nachrichten geteilt durch Verschickte Nachrichten
     * - Verhältnis in Prozent
     * @return
     */
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
                + "\n\t - Test2 errors " + errorCount
                + "\n== TEST Results ==";

        return testResults;
    }


    /**
     * Setzt Zähler für angekommene Nachrichten
     * @param count
     */
    static void setArrivedCount(int count){
        arrivedCount = count;
    }

    static void setArrivedMessage(String s){
        arrivedMessage = s;
    }
}