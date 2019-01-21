import java.net.UnknownHostException;

public class WindowSensor extends Sensor {

    public WindowSensor() throws UnknownHostException {
        this.sensorName = "WindowSensor-" + this.sensorNumber;
    }

    @Override
    protected String measure()
    {
        boolean measurement = super.random.nextBoolean();
        String result = "IsOpen: " + measurement;
        return result;
    }
}
