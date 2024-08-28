package it.interno.gestioneutentiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GestioneutentiServiceApplication {

	/*
		TODO:
		QUANDO HO SVILUPPATO QUESTO MICROSERVIZIO SONO IO E DIO SAPEVAMO COME FUNZIONAVA,
		ADESSO SOLO DIO LO SA.

		ABBIATENE CURA.
	*/

	public static void main(String[] args) {
		SpringApplication.run(GestioneutentiServiceApplication.class, args);
	}

}
