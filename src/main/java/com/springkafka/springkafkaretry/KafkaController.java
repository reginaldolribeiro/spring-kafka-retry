package com.springkafka.springkafkaretry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class KafkaController {

    @Value("${spring.kafka.topic}")
    private String topic;

    private final KafkaProducer kafkaProducer;

    @PostMapping("/publisher-default")
    public ResponseEntity<UserRequest> publisherUser(@RequestBody UserRequest userRequest) {
        log.info("[Controller] - POST Publisher Default - UserRequest {}", userRequest);
        kafkaProducer.sendObject(topic, userRequest);
        return ResponseEntity.ok(new UserRequest(1L, userRequest.name()));
    }
}
