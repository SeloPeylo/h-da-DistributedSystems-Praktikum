package Sensors;

public class SensorMain {
    public static void main(String args[])
    {
        BathSensor bath = new BathSensor();
        HumiditySensor humidity = new HumiditySensor();
        TempSensor temp = new TempSensor();
        WindowSensor window = new WindowSensor();

        Thread[] sensors = {new Thread(bath), new Thread(humidity), new Thread(temp), new Thread(window)};

        for(int i=0; i<sensors.length; i++)
        {
            sensors[i].start();
        }
    }
}
