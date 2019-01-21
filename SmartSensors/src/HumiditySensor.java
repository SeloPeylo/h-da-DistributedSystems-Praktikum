import java.net.UnknownHostException;

public class HumiditySensor extends Sensor {

    public HumiditySensor() throws UnknownHostException {
        this.sensorName = "HumiditySensor-" + this.sensorNumber;
    }

    @Override
    protected String measure() {
        float measurement = (super.random.nextInt() % 10000) / 100f;
        measurement = Math.abs(measurement);
        String result = "Percent: " + measurement;
        return result;
    }
}
