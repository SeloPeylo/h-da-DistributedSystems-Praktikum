package Sensors;
import java.net.*;

public class BathSensor extends Sensor {
    public BathSensor() throws UnknownHostException
    { super(); this.sensorName = "Bath-Sensor"; }

    public BathSensor(InetAddress address, int port) throws SocketException
    { super(address, port); this.sensorName = "Bath-Sensor";}

    public BathSensor(InetAddress address) throws SocketException
    { super(address); this.sensorName = "Bath-Sensor";}

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected String measure()
    {
        int measurement = super.random.nextInt() % 8000;
        String message = "BathSensor RPM: " + measurement;
        return message;
    }
}
