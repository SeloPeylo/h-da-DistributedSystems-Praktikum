import java.net.UnknownHostException;

public class TempSensor extends Sensor {

    public TempSensor() throws UnknownHostException {
        this.sensorName = "TempSensor-" + this.sensorNumber;
    }

    @Override
    protected String measure()
    {
        float measurement = (-1500f + (Math.abs(super.random.nextInt()) % 6000)) / 100 ; //-15 to 45 degrees
        String result = "Degrees: " + measurement;
        return result;
    }
}
