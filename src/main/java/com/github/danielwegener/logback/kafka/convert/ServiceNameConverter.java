package com.github.danielwegener.logback.kafka.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.github.danielwegener.logback.kafka.config.KafkaAppendProperties;
import com.github.danielwegener.logback.kafka.config.PropertiesHolder;

/**
 * @author: xuxd
 * @date: 2023/7/12 16:32
 **/
public class ServiceNameConverter extends ClassicConverter {

    private String service = "";

    public ServiceNameConverter() {
        String value = System.getProperty("service.name");
        if (value != null) {
            service = value;
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        if (service == null || service.isEmpty()) {
            KafkaAppendProperties properties = PropertiesHolder.getProperties();
            if (properties != null && properties.getAppName() != null) {
                service = properties.getAppName();
            }
        }
        return service;
    }
}
