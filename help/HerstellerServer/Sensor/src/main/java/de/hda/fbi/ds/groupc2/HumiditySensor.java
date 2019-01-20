package de.hda.fbi.ds.groupc2;

class HumiditySensor extends Sensor
{
    @Override
    public String getSensorData(){
        int temp = getRandomNumberInRange(20, 80);
        String tempString = Integer.toString(temp);
        String text = "Humidity: " + tempString;
        return text;
    }
}