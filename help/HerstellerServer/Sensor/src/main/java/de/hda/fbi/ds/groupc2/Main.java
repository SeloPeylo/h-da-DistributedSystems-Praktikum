package de.hda.fbi.ds.groupc2;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.stream.Stream;

public class Main {
    public static void main(String args[]) throws InterruptedException {
       /* if(args .length == 1) {
            Thread fan = new Thread(new BathSensor(args[0]));
            fan.start();

            Thread window = new Thread(new Window(args[0]));
            window.start();

            Thread humidity = new Thread(new HumiditySensor(args[0]));
            humidity.start();

            Thread temp = new Thread(new TempSensor(args[0]));
            temp.start();
        }
        else if(args .length == 2) {
            Thread fan = new Thread( new BathSensor(args[0], args[1]));
            fan.start();

            Thread window = new Thread( new Window(args[0], args[1]));
            window.start();

            Thread humidity = new Thread( new HumiditySensor(args[0], args[1]));
            humidity.start();

            Thread temp = new Thread( new TempSensor(args[0], args[1]));
            temp.start();
        } else { */
        Sensor bath = new BathSensor();
        Sensor humidity = new HumiditySensor();
        Sensor window = new WindowSensor();
        Sensor temperature = new TempSensor();

        Thread bathThread = new Thread(bath);
        Thread humidityThread = new Thread(humidity);
        Thread windowThread = new Thread(window);
        Thread temperatureThread = new Thread(temperature);

        bathThread.start();
        Thread.sleep(1000);
        humidityThread.start();
        Thread.sleep(1000);
        windowThread.start();
        Thread.sleep(1000);
        temperatureThread.start();

        bathThread.join();
        humidityThread.join();
        windowThread.join();
        temperatureThread.join();
    }
}


