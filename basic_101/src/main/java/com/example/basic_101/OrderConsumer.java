package com.example.basic_101;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderConsumer {
	
	// Standard SLF4J logger definition
    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);


    /**
     * topics: The topic we are listening to.
     * groupId: This is vital. If you have 10 instances of your app, 
     * Kafka uses this ID to balance the load between them.
     */
    @KafkaListener(topics = "orders-topic", groupId = "order-service-group")
    public void consume(String message) {
        log.info("-> Consumer received message: {}", message);
        
        // This is where your business logic starts (e.g., updating a database)
        processOrder(message);
    }

    private void processOrder(String message) {
        log.info("-> Successfully processed order: {}", message);
    }
}
