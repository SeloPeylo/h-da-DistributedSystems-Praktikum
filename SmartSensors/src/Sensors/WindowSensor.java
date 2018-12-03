package Sensors;
import java.net.*;

public class WindowSensor extends Sensor {
    private static int created = 1;

    public WindowSensor() throws UnknownHostException
    { super(); this.sensorName = "Window-Sensor " + created; created++; }

    @Override
    protected String measure()
    {
        boolean measurement = super.random.nextBoolean();
        String message = "WindowSensor IsOpen: " + measurement;
        return message;
    }
}
