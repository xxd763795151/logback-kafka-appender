package com.github.danielwegener.logback.kafka.config;

import java.util.*;

/**
 * @author: xuxd
 * @date: 2023/7/7 15:37
 **/

public class KafkaAppendProperties {

    private String fallbackAppender;

    private boolean discoverConfig = false;

    private Set<String> hideAppender = Collections.emptySet();

    private boolean hideSelf;

    private Properties producer = new Properties();

    public Map<String, Object> getProducer() {
        Map<String, Object> map = new HashMap<>();
        producer.forEach((k, v) -> map.put(k.toString(), v));
        return map;
    }

    public void setProducer(Properties producer) {
        this.producer = producer;
    }


    public String getFallbackAppender() {
        return fallbackAppender;
    }

    public void setFallbackAppender(String fallbackAppender) {
        this.fallbackAppender = fallbackAppender;
    }

    public boolean isDiscoverConfig() {
        return discoverConfig;
    }

    public void setDiscoverConfig(boolean discoverConfig) {
        this.discoverConfig = discoverConfig;
    }

    public Set<String> getHideAppender() {
        return hideAppender;
    }

    public void setHideAppender(Set<String> hideAppender) {
        this.hideAppender = hideAppender;
    }

    public boolean isHideSelf() {
        return hideSelf;
    }

    public void setHideSelf(boolean hideSelf) {
        this.hideSelf = hideSelf;
    }

    @Override
    public String toString() {
        return "KafkaProperties{" +
            "fallbackAppender='" + fallbackAppender + '\'' +
            ", discoverConfig=" + discoverConfig +
            ", hideAppender=" + hideAppender +
            ", hideSelf=" + hideSelf +
            ", producer=" + producer +
            '}';
    }
}
