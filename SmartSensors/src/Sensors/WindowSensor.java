package Sensors;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WindowSensor extends Sensor {
    private static int created = 1;

    public WindowSensor() throws UnknownHostException
    { super(); this.sensorName = "Window-Sensor " + created; created++; }

    @Override
    protected String measure()
    {
        boolean measurement = super.random.nextBoolean();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String message = timeStamp
                + " WindowSensor IsOpen: " + measurement;
        return message;
    }
}
