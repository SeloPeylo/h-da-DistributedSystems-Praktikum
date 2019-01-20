import org.eclipse.paho.client.mqttv3.*;

class HerstellerServer {


    public static void main(String[] args){

        String port = "1884";
        if (args.length > 0) {
            port = args[0];
        }

        try
        {

        MqttClient client = new MqttClient("tcp://iot.eclipse.org:1883", "hersteller1");
        client.setCallback(new SimpleMqttCallBack());
        client.connect();
        client.subscribe("sh_sensor_data");
        } catch (MqttException mex) {mex.getCause();}

    }

}
