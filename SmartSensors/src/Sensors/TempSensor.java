package Sensors;
import java.net.*;

public class TempSensor extends Sensor {

    public TempSensor() throws UnknownHostException
    { super(); this.sensorName = "Temp-Sensor"; }

    public TempSensor(InetAddress address, int port) throws SocketException
    { super(address, port); this.sensorName = "Temp-Sensor"; }

    public TempSensor(InetAddress address) throws SocketException
    { super(address); this.sensorName = "Temp-Sensor"; }

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected String measure()
    {
        float measurement = (-1500f + (super.random.nextInt() % 6000)) / 100 ; //-15 to 45 degrees
        String message = "TempSensor Degrees: " + measurement;
        return message;
    }
}
