package co.edu.unicauca.asae.proyecto.maestriacomputacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MaestriaComputacionApplication {

	private static final Logger log = LoggerFactory.getLogger(MaestriaComputacionApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(MaestriaComputacionApplication.class, args);
		log.error("Application running");
	}

}


