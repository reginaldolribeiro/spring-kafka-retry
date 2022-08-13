package com.springkafka.springkafkaretry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@Configuration
public class KafkaConfig {

    @Value("{spring.kafka.topic}")
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

    }
}
