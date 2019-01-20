import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonConverter Class.
 * This class reads the file1.json
 * parses JSONObjects to the relative java objects
 */
public class JsonConverter {
    private List<Object> objects = new ArrayList<>();
    private List<Object> luftfeuchtigkeitList = new ArrayList<>();
    private List<Object> temperatursList = new ArrayList<>();
    private List<Object> drehzahlList= new ArrayList<>();
    private List<Object> fensterList= new ArrayList<>();

    private JSONArray jsonObjects;


    public JsonConverter() {
        this.jsonParser();
    }

    public void jsonParser(){
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        URL resource = this.getClass().getResource("file1.json");

        try (FileReader reader = new FileReader(resource.getFile()))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            jsonObjects = (JSONArray) obj;
            System.out.println(jsonObjects);
            getKey(jsonObjects);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks every single object inside the JSONArray.
     * converts the JSONObject in to java object
     * adds the object to the relative list
     * @param sensorList
     */
    private void getKey(JSONArray sensorList){
        Object value;

        for(int i=0;i<sensorList.size();i++)
        {
            JSONObject jsonObject = (JSONObject) sensorList.get(i);
            if (jsonObject.keySet().contains("Luftfeuchtigkeit")){
                value = jsonObject.get("Luftfeuchtigkeit");
                objects.add(value);
                luftfeuchtigkeitList.add(value);
            }
            else if (jsonObject.keySet().contains("LuefterDrehzahl")){
                value = jsonObject.get("LuefterDrehzahl");
                objects.add(value);
                drehzahlList.add(value);
            }
            else if (jsonObject.keySet().contains("Temparatur")){
                value = jsonObject.get("Temparatur");
                objects.add(value);
                temperatursList.add(value);
            }
            else if (jsonObject.keySet().contains("Fenster")){
                value = jsonObject.get("Fenster");
                objects.add(value);
                fensterList.add(value);
            }
        }
    }

    /**
     * Converts objects to sensors.
     * @return
     */

    public List<SenderInfo> getSensorList(){
        List<SenderInfo> sensors = new ArrayList<>();
        try {
            for (Object each : objects) {
                JSONObject sensorObject = (JSONObject) each;

                String address = (String) sensorObject.get("Ip");
                InetAddress ip = InetAddress.getByName(address.substring(1,15));
                long port = (long) sensorObject.get("Port");

                SenderInfo sensor = new SenderInfo(ip , (int) port, (String) sensorObject.get("Data"));

                sensors.add(sensor);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return sensors;
    }

    public List<Object> getLuftfeuchtigkeitList() {
        return luftfeuchtigkeitList;
    }

    public List<Object> getTemperatursList() {
        return temperatursList;
    }

    public List<Object> getDrehzahlList() {
        return drehzahlList;
    }

    public List<Object> getFensterList() {
        return fensterList;
    }

    public void printJsonObjects(){
        for (Object sensor : objects) {
            JSONObject sensorObject = (JSONObject) sensor;

            System.out.println(sensorObject.get("Ip"));
            System.out.println(sensorObject.get("Port"));
            System.out.println(sensorObject.get("Data"));
        }
    }

    public void printSensors(){
        for (SenderInfo sensor : getSensorList()) {
            System.out.println(sensor.toString());
        }

    }
}
