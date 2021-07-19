package com.assignment.warehouse.stubs;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestLogAppender extends AppenderBase<ILoggingEvent> {

    private List<ILoggingEvent> events;

    public TestLogAppender() {
        events = new ArrayList<>();
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        setContext(lc);
        start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        events.add(event);
    }

    public void clear() {
        events = new ArrayList<>();
    }

    public String logs() {
        return events.stream()
                .map(ILoggingEvent::toString)
                .collect(Collectors.joining("\n"));
    }
}
