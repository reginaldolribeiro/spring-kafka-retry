package com.springkafka.springkafkaretry.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springkafka.springkafkaretry.model.UserRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.*;
import org.apache.avro.io.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaProducer {

    private static final EncoderFactory encoderFactory = EncoderFactory.get();
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTemplate<String, avro.UserRequest> kafkaAvroTemplate;

    private final KafkaTemplate<String, GenericRecord> kafkaAvroGenericRecordTemplate;
    private final KafkaTemplate<Object, byte[]> kafkaBytesTemplate;

    private final MyAvroSerializer serializer;

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
//        serializer.test();
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

        byte[] recordBytes = toAvroJson(payload, schema);

//        byte[] serialize = serialize(topic, payload, schema);
//        kafkaBytesTemplate.send(topic, serialize);
        kafkaBytesTemplate.send(topic, recordBytes);

    }

    public static byte[] toAvroJson(Object message, Schema schema){
        try(ByteArrayOutputStream stream = new ByteArrayOutputStream()){
            JsonEncoder encoder = encoderFactory.jsonEncoder(schema, stream);
            writeAvroToEncoder(message, schema, encoder);
            return stream.toByteArray();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private static void writeAvroToEncoder(Object message, Schema schema, Encoder encoder) throws IOException {
        DatumWriter<Object> writer = new GenericDatumWriter<>(schema);
        writer.write(toAvroRecord(message, schema), encoder);
        encoder.flush();
    }

    private static Object toAvroRecord(Object message, Schema schema) throws IOException {
        InputStream input = new ByteArrayInputStream(new ObjectMapper().writeValueAsString(message).getBytes());
        DataInputStream dataInputStream = new DataInputStream(input);
        DatumReader<Object> reader = new GenericDatumReader<>(schema);
        return reader.read(null, DecoderFactory.get().jsonDecoder(schema, dataInputStream));
    }

    public byte[] serialize(String topic, Object payload, Schema schema) {
        byte[] bytes = null;
        try {
            if (payload != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
                DatumWriter<Object> datumWriter = new GenericDatumWriter<>(schema);
                datumWriter.write(payload, binaryEncoder);
                binaryEncoder.flush();
                byteArrayOutputStream.close();
                bytes = byteArrayOutputStream.toByteArray();
                log.info("serialized payload='{}'",  new String(bytes, StandardCharsets.UTF_8));
//                log.info("serialized payload='{}'", DatatypeConverter.printHexBinary(bytes));
            }
        } catch (Exception e) {
            log.error("Unable to serialize payload ", e);
        }
        return bytes;
    }

}
