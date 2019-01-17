import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;


public class SensorData {

    private static Vector<JSONObject> dataList = new Vector<>();
    private MqttPublisher publisher;
    private String[] serverList = {"tcp://localhost:1884", "tcp://localhost:1885", "tcp://localhost:1886"};

    public SensorData() {
    }

    public void addData(JSONObject data) {
        dataList.add(data);
        byte[] payload = String.valueOf(data).getBytes();
        publisher = new MqttPublisher(serverList, payload);
        publisher.run();

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
}
