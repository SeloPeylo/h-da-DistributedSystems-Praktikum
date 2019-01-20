package de.hda.fbi.ds.groupc2;

class BathSensor extends Sensor
{
    public BathSensor(){
    }

    @Override
    public String getSensorData() {
        int temp = getRandomNumberInRange(300, 4000);
        String tempString = Integer.toString(temp);
        String text = "RPM Bath de.hda.fbi.ds.groupc2.Sensor: " + tempString;
        return text;
    }
}
