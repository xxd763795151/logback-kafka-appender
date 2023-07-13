package com.github.danielwegener.logback.kafka.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @author: xuxd
 * @date: 2023/7/12 16:42
 **/
public class ProcessIdConverter extends ClassicConverter {

    private String pid;

    public ProcessIdConverter() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        pid = runtimeMXBean.getName().split("@")[0];
    }

    @Override
    public String convert(ILoggingEvent event) {
        return pid;
    }
}
