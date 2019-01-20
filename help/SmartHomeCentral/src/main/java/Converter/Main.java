package Converter;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

public class Main {
    private double temp;
    private long pressure;
    private long humidity;
    private double tempMin;
    private double tempMax;

    @JsonProperty("temp")
    public double getTemp() { return temp; }
    @JsonProperty("temp")
    public void setTemp(double value) { this.temp = value; }

    @JsonProperty("pressure")
    public long getPressure() { return pressure; }
    @JsonProperty("pressure")
    public void setPressure(long value) { this.pressure = value; }

    @JsonProperty("humidity")
    public long getHumidity() { return humidity; }
    @JsonProperty("humidity")
    public void setHumidity(long value) { this.humidity = value; }

    @JsonProperty("temp_min")
    public double getTempMin() { return tempMin; }
    @JsonProperty("temp_min")
    public void setTempMin(double value) { this.tempMin = value; }

    @JsonProperty("temp_max")
    public double getTempMax() { return tempMax; }
    @JsonProperty("temp_max")
    public void setTempMax(double value) { this.tempMax = value; }
}
