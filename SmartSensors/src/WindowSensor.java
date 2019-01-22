import java.net.UnknownHostException;

public class WindowSensor extends Sensor {

    public WindowSensor() {
        this.sensorName = "WindowSensor-" + this.sensorID;
    }


    /**
     * Misst ob das Fenster offen oder zu ist.
     * @return
     */
    @Override
    protected String measure() {
        boolean measurement = super.random.nextBoolean();
        String result = "IsOpen: " + measurement;
        return result;
    }
}
