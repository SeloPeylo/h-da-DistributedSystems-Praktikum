import java.net.UnknownHostException;

public class TempSensor extends Sensor {

    public TempSensor() {
        this.sensorName = "TempSensor-" + this.sensorID;
    }

    /**
     * Misst die Temperatur zufällig zwischen -15 und +45 Grad Celsius
     * @return
     */
    @Override
    protected String measure() {
        float measurement = (-1500f + (Math.abs(super.random.nextInt()) % 6000)) / 100; //-15 to 45 degrees
        String result = "Degrees: " + measurement;
        return result;
    }
}
