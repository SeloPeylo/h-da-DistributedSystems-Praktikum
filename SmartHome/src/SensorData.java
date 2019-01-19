import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;


public class SensorData {

    private static Vector<JSONObject> dataList = new Vector<>();
    private MqttPublisher publisher;
    private byte[] payload = null;

    public SensorData(MqttPublisher publisher) {
        this.publisher = publisher;
    }

    public void addData(JSONObject data) {
        dataList.add(data);
        payload = String.valueOf(data).getBytes();
        publisher.putMessage(payload);
        new Thread(publisher).start();
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
