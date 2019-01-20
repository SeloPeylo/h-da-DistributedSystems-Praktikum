package de.hda.fbi.ds.groupc2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static de.hda.fbi.ds.groupc2.JsonReader.readJsonFromUrl;

public class Main {
    public static void main(String[] args) throws IOException {
        JSONObject json = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?id=4256460&appid=6a4e38edfb28f9c0cce652d87f31617e");
        System.out.println(json.toString());
        System.out.println(json.get("weather"));

        Weather w = new ObjectMapper()
                .readerFor(Weather.class)
                .readValue(json.toString());

        System.out.println("Temperatur: " + w.getMainTemp() + " Luftfeuchtigkeit: " + w.getHumidity() + " Pressure: " + w.getPressure() + " Stadt: " + w.getCityName());


        // Start the MQTT subscriber.
        Subscriber subscriber1 = new Subscriber("Hersteller 1");
        Subscriber subscriber2 = new Subscriber("Hersteller 2");
        Subscriber subscriber3 = new Subscriber("Hersteller 3");

        subscriber1.start();
        subscriber2.start();
        subscriber3.start();
    }
}
