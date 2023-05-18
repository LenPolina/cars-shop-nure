package com.implemica.kafka.component;

import org.springframework.stereotype.Component;

@Component
public class KafkaListener {
    @org.springframework.kafka.annotation.KafkaListener(topics = "kafkaTopicCatalog", groupId = "cars")
    void listener(String data){
        System.out.println("Listener received data "+ data+ "  :)");
    }
}
