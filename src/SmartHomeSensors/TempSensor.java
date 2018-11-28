package SmartHomeSensors;
import java.net.*;

public class TempSensor extends Sensor {

    public TempSensor()
    {
        super();
    }
    public TempSensor(InetAddress address, int port) throws SocketException
    {
        super(address, port);
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected String measure()
    {
        float measurement = (-1500f + (super.random.nextInt() % 6000)) / 100 ; //-15 to 45 degrees
        String message = "TempSensor Degrees: " + measurement + " IP: " + super.address + " Port: " + super.port;
        return message;
    }
}
