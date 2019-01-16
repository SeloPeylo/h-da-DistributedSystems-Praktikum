import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;


public class SensorData {

    private static Vector<JSONObject> dataList = new Vector<>();
    private static Vector<JSONObject> currentDataArray = new Vector<>();

    public SensorData() {
    }

    public void addData(JSONObject data) {
        dataList.add(data);
        try {

            switch (data.get("Sensortype").toString()) {
                case "BathSensor":
                    currentDataArray.set(0, data);
                    break;
                case "WindowSensor":
                    currentDataArray.set(1, data);
                    break;
                case "TempSensor":
                    currentDataArray.set(2, data);
                    break;
                case "HumiditySensor":
                    currentDataArray.set(3, data);
                    break;
                default:
                    break;
            }
        } catch (JSONException jex) {
            jex.printStackTrace();
        }
    }

    public Vector<JSONObject> getFilteredList(String key) {
        Vector<JSONObject> result = new Vector<>();

        if (dataList.size() < 1) {
            return null;
        }

        for (int i = 0; i < dataList.size(); i++) {
            try {
                if (dataList.get(i).get("Sensortype").equals(key)) {
                    result.add(dataList.get(i));
                }
            } catch (JSONException jex) {
                jex.printStackTrace();
            }
        }
        return result;
    }

    public Vector<JSONObject> getCurrentDataArray() {
        Vector<JSONObject> result = new Vector<>();
        if (dataList.size() < 1) {
            return null;
        }

        for (int i = 0; i < currentDataArray.size(); i++) {
            result.add(currentDataArray.get(i));
        }
        return result;

    }

}
