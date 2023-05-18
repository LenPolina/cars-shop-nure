package com.implemica.kafka;

import com.implemica.kafka.model.Result;
import com.implemica.kafka.model.Student;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class Controller {
    @Value("${kafka.reuest.topic}")
    private String requestTopic;
    @Autowired
    private ReplyingKafkaTemplate<String, Student, Result> replyingKafkaTemplate;

    @PostMapping("/get-result")
    public ResponseEntity<Result> getObject(@RequestBody Student student)
        throws InterruptedException, ExecutionException, TimeoutException {
        ProducerRecord<String, Student> record = new ProducerRecord<>(requestTopic, null, student.getRegistrationNumber(), student);
        RequestReplyFuture<String, Student, Result> future = replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Result> response = future.get(10000L, TimeUnit.MILLISECONDS);
        System.out.println("dkfjdfj");
        return new ResponseEntity<>(response.value(), HttpStatus.OK);
    }
}
