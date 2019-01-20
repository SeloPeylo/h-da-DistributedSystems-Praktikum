import java.net.UnknownHostException;
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
        return timeStamp + " WindowSensor IsOpen: " + measurement;
    }
}
