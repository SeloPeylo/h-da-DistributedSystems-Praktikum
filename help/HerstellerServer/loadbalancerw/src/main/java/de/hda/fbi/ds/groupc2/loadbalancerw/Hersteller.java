package de.hda.fbi.ds.groupc2.loadbalancerw;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Hersteller {



    public static void main(String[] args) throws MqttException {
		SpringApplication.run(Hersteller.class, args);
	}
}

