package com.springkafka.springkafkaretry.producer;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerializer;
import org.springframework.stereotype.Component;

@Component
public class MyAvroSerializer extends AbstractKafkaAvroSerializer {

    /*public void test(){
        try {
            System.out.println(schemaRegistry);
            ParsedSchema schema = schemaRegistry.getSchemaById(1);
            System.out.println(schema);
        } catch (IOException | RestClientException e) {
            throw new RuntimeException(e);
        }


    }*/
}
