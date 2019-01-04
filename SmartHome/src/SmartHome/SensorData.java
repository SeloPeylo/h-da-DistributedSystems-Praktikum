package SmartHome;

import java.util.Vector;

public class SensorData {

    private static Vector<String> data = new Vector<>();

    public SensorData(){}

    public void addData(String data)
    {
        this.data.add(data);
    }

    public String getData()
    {
        if(this.data.size() >= 1) { return null; }
        return this.data.remove(0);
    }
}
