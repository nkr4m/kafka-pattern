package com.example.basic_101;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kafka")
@RequiredArgsConstructor // This generates the constructor for the Producer final field
public class OrderController {

	@Autowired
    OrderProducer orderProducer;

    /**
     * URL: http://localhost:8080/api/v1/kafka/publish?msg=YourMessageHere
     */
    @GetMapping("/publish")
    public String publishMessage(@RequestParam("msg") String message) {
        orderProducer.sendMessage(message);
        return "Message successfully sent to Kafka Topic: orders-topic";
    }
}