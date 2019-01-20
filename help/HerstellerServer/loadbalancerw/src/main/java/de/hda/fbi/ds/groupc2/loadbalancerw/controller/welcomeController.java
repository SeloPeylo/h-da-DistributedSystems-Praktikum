package de.hda.fbi.ds.groupc2.loadbalancerw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hda.fbi.ds.groupc2.loadbalancerw.Subscriber;
import de.hda.fbi.ds.groupc2.loadbalancerw.Weather;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static de.hda.fbi.ds.groupc2.loadbalancerw.JsonReader.readJsonFromUrl;


@RestController
public class welcomeController {
    private String wetter;

    JSONObject json = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?id=4256460&appid=6a4e38edfb28f9c0cce652d87f31617e");

    Weather w = new ObjectMapper()
            .readerFor(Weather.class)
            .readValue(json.toString());

    @Value("${server.port}")
    String port;

    public welcomeController() throws IOException, JSONException {
    }

    @RequestMapping(value = "/")
    public String home() throws MqttException {
        Subscriber s = new Subscriber("h1");
        s.start();

        wetter = "Temperatur: " + w.getMainTemp() + " Luftfeuchtigkeit: " + w.getHumidity() + " Pressure: " + w.getPressure() + " Stadt: " + w.getCityName();
        return "Portnummer" + port + "\n" + wetter;
    }

    @RequestMapping("/greeting")
    public String hello() {
        Subscriber s = new Subscriber("h1");
        s.start();
        return "Hello from a service running at port: " + port + "!";
    }


}