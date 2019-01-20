package de.hda.fbi.ds.groupc2.loadbalancerw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(value = {"coord", "weather", "base", "visibility", "clouds", "dt", "sys", "id", "id","cod", "wind"})
public class Weather {
    private double mainTemp;
    private int humidity;
    private int pressure;
    private String cityName;

    @JsonProperty("name")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getMainTemp() {
        return mainTemp;
    }

    public void setMainTemp(double mainTemp) {
        this.mainTemp = mainTemp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("main")
    private void unpackNested(Map<String,Object> main) {
        this.mainTemp = (double)main.get("temp");
        this.humidity = (int)main.get("humidity");
        this.pressure = (int)main.get("pressure");
    }
}
