package com.company.Sensor;
import java.io.IOException;
import java.net.*;

public class HumiditySensor extends Sensor {

    public HumiditySensor()
    {
        super();
    }
    public HumiditySensor(InetAddress address, int port) throws SocketException
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
        float measurement = (super.random.nextInt() % 10000) / 100f;
        String message = "HumiditySensor Percent: " + measurement + " IP: " + super.address + " Port: " + super.port;
        return message;
    }
}
