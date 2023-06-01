package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.BinderHeaders;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.solace.samples.spring.common.SensorReading;
import com.solace.samples.spring.common.SensorReading.BaseUnit;

@SpringBootApplication
@RestController
public class StreambridgeRestControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreambridgeRestControllerApplication.class, args);
	}
	
	@Autowired 
	StreamBridge streamBridge; 
	
	@GetMapping(value = "/temperature/publish/dynamic_topic/{location}/{temperature}")
	public void publishTemperatureDynamicTopic(
	        @PathVariable("location") final String location,
	        @PathVariable("temperature") final double temperature
	) {
	    SensorReading reading = new SensorReading();
	    reading.setSensorID(location);
	    reading.setTemperature(temperature);
	    reading.setBaseUnit(BaseUnit.CELSIUS);

	    System.out.println("Emitting " + reading);

	    Message<SensorReading> message = MessageBuilder
	            .withPayload(reading)
	            .setHeader(
	                    BinderHeaders.TARGET_DESTINATION,
	                    "test/topic/" + location)
	            .build();
	    streamBridge.send("emitTemperatureSensorDynamic-out-0", message);
	}

}
