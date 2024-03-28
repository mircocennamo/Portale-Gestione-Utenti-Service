package it.interno.gestioneutentiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class GestioneutentiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestioneutentiServiceApplication.class, args);
	}

}
