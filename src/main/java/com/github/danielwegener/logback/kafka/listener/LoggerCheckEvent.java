package com.github.danielwegener.logback.kafka.listener;

import org.springframework.context.ApplicationEvent;

/**
 * @author: xuxd
 * @date: 2023/7/11 17:30
 **/
public class LoggerCheckEvent extends ApplicationEvent {
    public LoggerCheckEvent(Object source) {
        super(source);
    }
}
