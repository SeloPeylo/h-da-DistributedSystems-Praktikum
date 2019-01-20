package de.hda.fbi.ds.groupc2;

class TempSensor extends Sensor
{
    public TempSensor(){
    }

    @Override
    public String getSensorData() {
        int temp = getRandomNumberInRange(10, 40);
        String tempString = Integer.toString(temp);
        String text = "Temperature: " + tempString;
        return text;
    }
}