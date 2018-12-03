package Sensors;
import java.net.*;

public class TempSensor extends Sensor {
    private static int created = 1;

    public TempSensor() throws UnknownHostException
    { super(); this.sensorName = "Temp-Sensor " + created; created++; }

    @Override
    protected String measure()
    {
        float measurement = (-1500f + (super.random.nextInt() % 6000)) / 100 ; //-15 to 45 degrees
        String message = "TempSensor Degrees: " + measurement;
        return message;
    }
}
