package com.springkafka.springkafkaretry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = { "default-topic" })
    public void consumerDefault(ConsumerRecord<String, UserRequest> message){
        log.info("Receiving message {}", message);
//        throw new IllegalArgumentException("IllegalArgumentException!!!");
        throw new RuntimeException("Exception!!!!!!!");
    }

}
