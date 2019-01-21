import java.net.UnknownHostException;

public class BathSensor extends Sensor {

    public BathSensor() throws UnknownHostException{
        this.sensorName = "BathSensor-" + this.sensorNumber;
    }

    @Override
    protected String measure()
    {
        int measurement = super.random.nextInt() % 8000;
        measurement = Math.abs(measurement);
        String result = "RPM: " + measurement;
        return result;
    }
}
