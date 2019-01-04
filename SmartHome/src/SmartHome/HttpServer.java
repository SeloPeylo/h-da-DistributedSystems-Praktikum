package SmartHome;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer implements Runnable{

    private ServerSocket socket = new ServerSocket(8080);
    SensorData sensorData;

    public HttpServer(SensorData sensorData) throws IOException
    {
        this.sensorData = sensorData;
    }

    @Override
    public void run() {

    }
}
