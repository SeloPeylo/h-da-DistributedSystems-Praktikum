package Sensors;
import java.net.*;

public class WindowSensor extends Sensor {

    public WindowSensor()
    {
        super();
    }
    public WindowSensor(InetAddress address, int port) throws SocketException
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
        boolean measurement = super.random.nextBoolean();
        String message = "WindowSensor IsOpen: " + measurement + " IP: " + super.address + " Port: " + super.port;
        return message;
    }
}
