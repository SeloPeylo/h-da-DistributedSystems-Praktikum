package Sensors;
import java.net.*;

public class HumiditySensor extends Sensor {
    private static int created = 1;

    public HumiditySensor() throws UnknownHostException
    { super(); this.sensorName = "Humidity-Sensor " + created; created++;}

    @Override
    protected String measure()
    {
        float measurement = (super.random.nextInt() % 10000) / 100f;
        String message = "HumiditySensor Percent: " + measurement;
        return message;
    }
}
