package com.springkafka.springkafkaretry.producer;

import com.springkafka.springkafkaretry.model.UserRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTemplate<String, avro.UserRequest> kafkaAvroTemplate;

    public void sendObject(String topic, Object payload) {
        log.info("sending payload='{}' to topic='{}'", payload, topic);
        kafkaTemplate.send(topic, payload);
    }

    public void sendAvro(String topic, UserRequest payload){
        log.info("sending avro payload='{}' to topic='{}'", payload, topic);

        avro.UserRequest user = avro.UserRequest
                .newBuilder()
                .setId(payload.id().intValue())
                .setName(payload.name())
                .build();

        kafkaAvroTemplate.send(topic, user);
    }
}
