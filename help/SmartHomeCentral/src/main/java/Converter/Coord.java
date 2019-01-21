package Converter;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

public class Coord {
    private double lon;
    private double lat;

    @JsonProperty("lon")
    public double getLon() { return lon; }
    @JsonProperty("lon")
    public void setLon(double value) { this.lon = value; }

    @JsonProperty("lat")
    public double getLat() { return lat; }
    @JsonProperty("lat")
    public void setLat(double value) { this.lat = value; }
}