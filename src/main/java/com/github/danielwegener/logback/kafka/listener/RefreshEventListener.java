package com.github.danielwegener.logback.kafka.listener;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.github.danielwegener.logback.kafka.FallbackAppender;
import com.github.danielwegener.logback.kafka.config.PropertiesHolder;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: xuxd
 * @date: 2023/7/10 14:45
 **/
public class RefreshEventListener implements SmartApplicationListener {


    private Set<String> lastHideAppender = new HashSet<>();

    /**
     * key: logger name + append name;
     */
    private Map<String, Appender<ILoggingEvent>> cache = new HashMap<>();

    private Map<String, Appender<ILoggingEvent>> appenderNameMap = new HashMap<>();

    private Set<String> loggerNameSet = new HashSet<>();

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return LoggerCheckEvent.class.isAssignableFrom(eventType)
            || ApplicationReadyEvent.class.isAssignableFrom(eventType)
            || eventType.toString().contains("Refresh")
            || eventType.toString().contains("refresh");
    }

    @Override
    public int getOrder() {
        return SmartApplicationListener.super.getOrder();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        ILoggerFactory factory = LoggerFactory.getILoggerFactory();
        LoggerContext context = (LoggerContext) factory;
        List<Logger> loggerList = context.getLoggerList();

        Set<String> hideAppender = PropertiesHolder.getProperties().getHideAppender();

        synchronized (RefreshEventListener.class) {
            // only process one of them.
            if (event instanceof LoggerCheckEvent) {
                boolean hasChange = checkAppenderHasChange(loggerList);
                if (!hasChange) {
                    return;
                }
            } else {
                if (lastHideAppender.equals(hideAppender)) {
                    return;
                }
            }
            // compute which appender need add or remove
            Set<String> removeFromCurrent = hideAppender.stream().filter(e -> !lastHideAppender.contains(e)).collect(Collectors.toSet());
            Set<String> add2Current = lastHideAppender.stream().filter(e -> !hideAppender.contains(e)).collect(Collectors.toSet());

            loggerList.forEach(logger -> {
                Iterator<Appender<ILoggingEvent>> iterator = logger.iteratorForAppenders();
                while (iterator.hasNext()) {
                    Appender<ILoggingEvent> appender = iterator.next();
                    appenderNameMap.put(appender.getName(), appender);
                    if (FallbackAppender.isUnInitialize() && FallbackAppender.hit(appender.getName())) {
                        FallbackAppender.setAppender(appender);
                    }
                }
                for (String appenderName : removeFromCurrent) {
                    Appender<ILoggingEvent> appender = logger.getAppender(appenderName);
                    if (appender == null) {
                        continue;
                    }
                    appenderNameMap.put(appenderName, appender);
                    if (logger.detachAppender(appender)) {
                        cache.put(getLoggerAppenderKey(logger.getName(), appenderName), appender);
                    }
                }

                for (String appenderName : add2Current) {
                    String key = getLoggerAppenderKey(logger.getName(), appenderName);
                    if (!cache.containsKey(key)) {
                        continue;
                    }

                    if (!appenderNameMap.containsKey(appenderName)) {
                        continue;
                    }
                    logger.addAppender(appenderNameMap.get(appenderName));
                }
            });
            lastHideAppender.clear();
            lastHideAppender.addAll(hideAppender);
        }
    }

    /**
     * @return true: change, false: not change.
     */
    private boolean checkAppenderHasChange(List<Logger> loggerList) {
        Set<String> set = loggerList.stream().map(Logger::getName).collect(Collectors.toSet());
        if (!loggerNameSet.equals(set)) {
            loggerNameSet = set;
            return true;
        }
        return false;
    }

    private String getLoggerAppenderKey(String logger, String appender) {
        return appender + "@" + logger;
    }
}
