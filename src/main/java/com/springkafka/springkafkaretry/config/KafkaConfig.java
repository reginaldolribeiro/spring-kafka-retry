package com.springkafka.springkafkaretry.config;

//@Configuration
public class KafkaConfig {

    /*@Value("{spring.kafka.topic}")
    private String topic;

    @Bean
    public RetryTopicConfiguration retryTopicConfiguration(KafkaTemplate<String, Object> kafkaTemplate){
        return RetryTopicConfigurationBuilder
                .newInstance()
//                .fixedBackOff(10000)
                .exponentialBackoff(3000, 2, 60000)
                .maxAttempts(4)
                .includeTopic("default-topic")
                .notRetryOn(IllegalArgumentException.class)
                .create(kafkaTemplate);

    }*/
}
