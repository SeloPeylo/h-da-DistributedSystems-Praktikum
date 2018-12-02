package Sensors;
import java.net.*;

public class HumiditySensor extends Sensor {

    public HumiditySensor() throws UnknownHostException
    { super(); this.sensorName = "Humidity-Sensor";}

    public HumiditySensor(InetAddress address, int port) throws SocketException
    { super(address, port); this.sensorName = "Humidity-Sensor"; }

    public HumiditySensor(InetAddress address) throws SocketException
    { super(address); this.sensorName = "Humidity-Sensor"; }

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected String measure()
    {
        float measurement = (super.random.nextInt() % 10000) / 100f;
        String message = "HumiditySensor Percent: " + measurement;
        return message;
    }
}
