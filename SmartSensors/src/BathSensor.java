import java.net.UnknownHostException;

public class BathSensor extends Sensor {

    public BathSensor(){
        this.sensorName = "BathSensor-" + this.sensorID;
    }


    /**
     * Misst die Drehzahl des Lüfters in RPM 0 bis 8000
     * @return
     */
    @Override
    protected String measure()
    {
        int measurement = super.random.nextInt() % 8000;
        measurement = Math.abs(measurement);
        String result = "RPM: " + measurement;
        return result;
    }
}
