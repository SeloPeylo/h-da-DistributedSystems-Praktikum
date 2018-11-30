package Sensors;
import java.net.*;

public class WindowSensor extends Sensor {

    public WindowSensor() throws UnknownHostException
    { super(); this.sensorName = "Window-Sensor"; }

    public WindowSensor(InetAddress address, int port) throws SocketException
    { super(address, port); this.sensorName = "Window-Sensor"; }

    public WindowSensor(InetAddress address) throws SocketException
    { super(address); this.sensorName = "Window-Sensor"; }

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
