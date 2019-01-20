package Converter;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

public class Clouds {
    private long all;

    @JsonProperty("all")
    public long getAll() { return all; }
    @JsonProperty("all")
    public void setAll(long value) { this.all = value; }
}
