package de.hda.fbi.ds.groupc2;

class WindowSensor extends Sensor
{
    public WindowSensor(){
    }

    @Override
    public String getSensorData() {
        int windowState = getRandomNumberInRange(0, 1);
        if(windowState == 0) {
            return "Fensterzustand: Fenster offen";
        }
        return "Fensterzustand: Fenster geschlossen";
    }
}