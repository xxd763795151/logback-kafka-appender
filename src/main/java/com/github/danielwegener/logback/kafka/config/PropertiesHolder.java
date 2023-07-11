package com.github.danielwegener.logback.kafka.config;

/**
 * @author: xuxd
 * @date: 2023/7/10 9:15
 **/
public class PropertiesHolder {


    private static KafkaAppendProperties properties;

    public PropertiesHolder(KafkaAppendProperties properties) {
        PropertiesHolder.properties = properties;
    }

    public static KafkaAppendProperties getProperties() {
        return properties;
    }

    public static boolean propertiesCanUse() {
        return properties != null && properties.isDiscoverConfig();
    }
}
