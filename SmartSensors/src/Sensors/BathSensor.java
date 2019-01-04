package Sensors;
import java.net.*;

public class BathSensor extends Sensor {
    private static int created = 1;

    public BathSensor() throws UnknownHostException
    { super(); this.sensorName = "Bath-Sensor " + created; created++; }

    @Override
    protected String measure()
    {
        int measurement = super.random.nextInt() % 8000;
        measurement = Math.abs(measurement);
        String message = "BathSensor RPM: " + measurement;
        return message;
    }
}
