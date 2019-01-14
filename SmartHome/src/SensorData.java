import org.json.JSONException;
import org.json.JSONObject;
import java.util.Vector;


public class SensorData{

    private static Vector<JSONObject> dataList = new Vector<>();

    public SensorData(){}

    public void addData(JSONObject data)
    {
        dataList.add(data);
    }

    public Vector<JSONObject> getFilteredList(String key)
    {
        Vector<JSONObject> result = new Vector<>();

        if(dataList.size() < 1) { return null; }

        for(int i=0; i<dataList.size(); i++)
        {
            try{
                if(dataList.get(i).get("Sensortype").equals(key))
                {
                    result.add(dataList.get(i));
                }
            } catch(JSONException jex) {jex.printStackTrace();}
        }
        return result;
    }
}
