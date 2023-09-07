package com.github.danielwegener.logback.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

/**
 * @author: xuxd
 * @date: 2023/7/12 11:35
 **/
public class FallbackAppender {

    public static String name;

    private static Appender<ILoggingEvent> appender;

    public static boolean isUnInitialize() {
        return appender == null;
    }


    public static void setAppender(Appender<ILoggingEvent> appender) {
        FallbackAppender.appender = appender;
    }


    public static void append(ILoggingEvent event) {
        if (appender != null) {
            appender.doAppend(event);
        }
    }

    public static boolean knowName() {
        return name != null;
    }

    public static boolean hit(String guess) {
        return knowName() && name.equalsIgnoreCase(guess);
    }
}
