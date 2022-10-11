package com.springkafka.springkafkaretry.producer;

import com.springkafka.springkafkaretry.model.UserRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTemplate<String, avro.UserRequest> kafkaAvroTemplate;

    private final KafkaTemplate<String, GenericRecord> kafkaAvroGenericRecordTemplate;

    public void sendObject(String topic, Object payload) {
        log.info("sending payload='{}' to topic='{}'", payload, topic);
        kafkaTemplate.send(topic, payload);
    }

    public void sendAvro(String topic, UserRequest payload){
        log.info("sending avro payload='{}' to topic='{}'", payload, topic);

        avro.UserRequest user = avro.UserRequest
                .newBuilder()
//                .setId(payload.id().intValue())
                .setId(payload.id())
                .setName(payload.name())
                .build();

        kafkaAvroTemplate.send(topic, user);

    }

    public void sendAvroGenericRecord(String topic, UserRequest payload){
        log.info("sending avro payload='{}' to topic='{}'", payload, topic);

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse("{\n" +
                "  \"type\": \"record\",\n" +
                "  \"name\": \"UserRequest\",\n" +
                "  \"namespace\": \"avro\",\n" +
                "  \"fields\": [\n" +
                "    {\n" +
                "      \"name\": \"id\",\n" +
                "      \"type\": \"int\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"name\",\n" +
                "      \"type\": \"string\"\n" +
                "    }\n" +
                "  ]\n" +
                "}");


        /*GenericRecordBuilder genericRecordBuilder = new GenericRecordBuilder(schema);
        genericRecordBuilder.set("id", payload.id());
        genericRecordBuilder.set("name", payload.name());
        GenericData.Record record = genericRecordBuilder.build();*/
//        GenericData.Record record = new GenericData.Record(schema);

        GenericData.Record record = new GenericRecordBuilder(schema)
                .set("id", payload.id())
                .set("name", payload.name())
                .build();

        kafkaAvroGenericRecordTemplate.send(topic, record);

    }
}
