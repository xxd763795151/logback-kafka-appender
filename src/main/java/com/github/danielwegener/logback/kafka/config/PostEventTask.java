package com.github.danielwegener.logback.kafka.config;

import com.github.danielwegener.logback.kafka.listener.LoggerCheckEvent;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: xuxd
 * @date: 2023/7/11 18:06
 **/
public class PostEventTask implements SmartInitializingSingleton {


    private final ApplicationEventPublisher publisher;

    public PostEventTask(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void afterSingletonsInstantiated() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                publisher.publishEvent(new LoggerCheckEvent(this));
            }
        }, 1000 * 60, 3000);

    }
}
