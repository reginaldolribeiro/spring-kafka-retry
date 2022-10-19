package com.springkafka.springkafkaretry.controller;

import com.springkafka.springkafkaretry.model.UserRequest;
import com.springkafka.springkafkaretry.producer.KafkaProducer;
import com.springkafka.springkafkaretry.service.RetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class KafkaController {

    @Value("${spring.kafka.topic}")
    private String topic;

    private final KafkaProducer kafkaProducer;
    private final RetryService retryService;

    @PostMapping("/publisher-default")
    public ResponseEntity<UserRequest> publisherUser(@RequestBody UserRequest userRequest) {
        log.info("[Controller] - POST Publisher Default - UserRequest {}", userRequest);
        kafkaProducer.sendObject(topic, userRequest);
        return ResponseEntity.ok(new UserRequest(1, userRequest.name()));
    }

    @PostMapping("/avro")
    public void avro(@RequestBody UserRequest userRequest) {
        log.info("[Controller] - POST Publisher AVRO - UserRequest {}", userRequest);
        kafkaProducer.sendAvro("avro-topic", userRequest);
//        kafkaProducer.sendObject(topic, userRequest);
    }

    @PostMapping("/avro-generic-record")
    public void avroGenericRecord(@RequestBody UserRequest userRequest) {
        log.info("[Controller] - POST Publisher AVRO - UserRequest {}", userRequest);
        kafkaProducer.sendAvroGenericRecord("avro-generic-topic", userRequest);
//        kafkaProducer.sendObject(topic, userRequest);
    }
    
    @GetMapping("/retry/{value}")
    public void retry(@PathVariable("value") int value){
        retryService.retry(value);
        System.out.println("*** Respondendo o GET /retry/{value}");
        System.out.println("*** Fim do retry2");
    }
}
