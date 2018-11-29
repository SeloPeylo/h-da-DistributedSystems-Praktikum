package Sensors;
import java.net.*;

public class BathSensor extends Sensor {

    public BathSensor()
    {
        super();
    }
    public BathSensor(InetAddress address, int port) throws SocketException
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
        int measurement = super.random.nextInt() % 8000;
        String message = "BathSensor RPM: " + measurement + " IP: " + super.address + " Port: " + super.port;
        return message;
    }
}
