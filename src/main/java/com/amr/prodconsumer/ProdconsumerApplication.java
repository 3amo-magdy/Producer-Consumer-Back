package com.amr.prodconsumer;

import java.util.UUID;

import com.amr.prodconsumer.Simulation.Simulator;
import com.amr.prodconsumer.components.M;
import com.amr.prodconsumer.components.Q;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ProdconsumerApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(ProdconsumerApplication.class, args);
		// Simulator s= new Simulator();
		// Q q1 = s.addQueue();
		// Q q2=  s.addQueue();
		// M m1=  s.addService();
		// s.linkProvider(m1.getId(), q1.getId());
		// s.setConsumer(m1.getId(),q2.getId());
		// s.setInputQueue(q1.getId());
		// s.setInputRate(10);l
		// s.startSimulating();


	}
}
