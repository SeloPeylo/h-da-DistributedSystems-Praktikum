package Sensors;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BathSensor extends Sensor {
    private static int created = 1;

    public BathSensor() throws UnknownHostException
    { super(); this.sensorName = "Bath-Sensor " + created; created++; }

    @Override
    protected String measure()
    {
        int measurement = super.random.nextInt() % 8000;
        measurement = Math.abs(measurement);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String message = timeStamp
                + " BathSensor RPM: " + measurement;
        return message;
    }
}
