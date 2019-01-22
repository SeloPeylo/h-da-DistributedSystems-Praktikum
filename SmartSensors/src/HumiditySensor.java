import java.net.UnknownHostException;

public class HumiditySensor extends Sensor {

    public HumiditySensor() {
        this.sensorName = "HumiditySensor-" + this.sensorID;
    }

    /**
     * Misst die Luftfeuchtigkeit in Prozent zufällig 0-100%
     * @return
     */
    @Override
    protected String measure() {
        float measurement = (super.random.nextInt() % 10000) / 100f;
        measurement = Math.abs(measurement);
        String result = "Percent: " + measurement;
        return result;
    }
}
