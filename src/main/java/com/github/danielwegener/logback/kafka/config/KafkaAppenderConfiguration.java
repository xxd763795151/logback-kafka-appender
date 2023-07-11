package com.github.danielwegener.logback.kafka.config;

import com.github.danielwegener.logback.kafka.listener.RefreshEventListener;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author: xuxd
 * @date: 2023/7/10 9:26
 **/
@Configuration
public class KafkaAppenderConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "logback.kafka")
    public KafkaAppendProperties kafkaProperties() {
        KafkaAppendProperties kafkaAppendProperties = new KafkaAppendProperties();
        kafkaAppendProperties.setDiscoverConfig(true);
        return kafkaAppendProperties;
    }

    @Bean
    @DependsOn("kafkaProperties")
    public PropertiesHolder kafkaPropertiesHolder(KafkaAppendProperties kafkaAppendProperties) {
        return new PropertiesHolder(kafkaAppendProperties);
    }

    @Bean
    public RefreshEventListener refreshEventListener() {
        return new RefreshEventListener();
    }

    @Bean
    public PostEventTask postEventTask(ApplicationEventPublisher publisher) {
        return new PostEventTask(publisher);
    }
}
