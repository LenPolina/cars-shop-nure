package com.implemica.kafka.component;

import com.implemica.kafka.model.Result;
import com.implemica.kafka.model.Student;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class StudentResultCalculator {
    @KafkaListener(topics = "${kafka.reuest.topic}", groupId = "${kafka.group.id}")
    @SendTo
    public Result handle(Student student) {
        System.out.println("Calculating Result...");
        double total = ThreadLocalRandom.current().nextDouble(2.5, 9.9);
        Result result = new Result();
        result.setName(student.getName());
        result.setResult((total > 3.5) ? "Pass" : "Fail");
        result.setPercentage(String.valueOf(total * 10).substring(0, 4) + "%");
        return result;
    }
}
