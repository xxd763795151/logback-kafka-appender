package com.github.danielwegener.logback.kafka;

import com.github.danielwegener.logback.kafka.config.KafkaAppenderConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: xuxd
 * @date: 2023/7/10 9:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(KafkaAppenderConfiguration.class)
public @interface EnableLogbackConfigDiscover {
}
