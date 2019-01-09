package SmartHome;

import java.util.Vector;

public class SensorData implements Runnable{

    private static Vector<String> data = new Vector<>();
    private static Vector<String> bathData = new Vector<>();
    private static Vector<String> humidityData = new Vector<>();
    private static Vector<String> tempData = new Vector<>();
    private static Vector<String> windowData = new Vector<>();


    public SensorData(){}

    public void addData(String data)
    {
        this.data.add(data);
    }

    public Vector<String> getCopy()
    {
        Vector<String> copy = new Vector<>();
        for(int i=0; i<this.data.size(); i++)
        {
            copy.add(this.data.get(i));
        }
        return copy;
    }
    public Vector<String> getCopy(String sensortype)
    {
        Vector<String> copy = new Vector<>();
        if(sensortype.equals("Bath")) {
            for(int i=0; i<this.bathData.size(); i++)
            {
                copy.add(this.bathData.get(i));
            }
            return copy;
        }
        if(sensortype.equals("Humidity")) {
            for(int i=0; i<this.humidityData.size(); i++)
            {
                copy.add(this.humidityData.get(i));
            }
            return copy;
        }
        if(sensortype.equals("Temp")) {
            for(int i=0; i<this.tempData.size(); i++)
            {
                copy.add(this.tempData.get(i));
            }
            return copy;
        }
        if(sensortype.equals("Window")) {
            for(int i=0; i<this.windowData.size(); i++)
            {
                copy.add(this.windowData.get(i));
            }
            return copy;
        }
        return null;
    }


    @Override
    public void run() {
        String s;
        String[] split;
        int index;

        while(true)
        {
            try{
                Thread.sleep(2000);
            } catch(InterruptedException ie) {ie.printStackTrace();}
            while(this.data.size() >= 1)
            {
                s = this.data.remove(0);
                split = s.split(" ");
                s = split[0] + " " + split[1] + " " + split[3] + " " + split[4];
                switch(split[2]){
                    case "BathSensor":
                        this.bathData.add(s);
                        break;
                    case "HumiditySensor":
                        this.humidityData.add(s);
                        break;
                    case "TempSensor":
                        this.tempData.add(s);
                        break;
                    case "WindowSensor":
                        this.windowData.add(s);
                        break;
                    default:
                        break;
                }

            }
        }
    }
}
