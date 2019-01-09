package Sensors;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TempSensor extends Sensor {
    private static int created = 1;

    public TempSensor() throws UnknownHostException
    { super(); this.sensorName = "Temp-Sensor " + created; created++; }

    @Override
    protected String measure()
    {
        float measurement = (-1500f + (Math.abs(super.random.nextInt()) % 6000)) / 100 ; //-15 to 45 degrees
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String message = timeStamp + " TempSensor Degrees: " + measurement;
        return message;
    }
}
