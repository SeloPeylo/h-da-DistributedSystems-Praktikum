package Sensors;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HumiditySensor extends Sensor {
    private static int created = 1;

    public HumiditySensor() throws UnknownHostException
    { super(); this.sensorName = "Humidity-Sensor " + created; created++;}

    @Override
    protected String measure()
    {
        float measurement = (super.random.nextInt() % 10000) / 100f;
        measurement = Math.abs(measurement);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String message = timeStamp +
                " HumiditySensor Percent: " + measurement;
        return message;
    }
}
