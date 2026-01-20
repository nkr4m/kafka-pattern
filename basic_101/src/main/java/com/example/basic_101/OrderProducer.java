package com.example.basic_101;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@Slf4j
public class OrderProducer {
	
	// Standard SLF4J logger definition
    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        log.info("Producing message: {}", message);

        // This is the production way: handle the result of the 'send'
        this.kafkaTemplate.send("orders-topic", message)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Sent message=[{}] with offset=[{}] to partition=[{}]", 
                             message, 
                             result.getRecordMetadata().offset(),
                             result.getRecordMetadata().partition());
                } else {
                    log.error("Unable to send message=[{}] due to : {}", message, ex.getMessage());
                }
            });
    }
}
