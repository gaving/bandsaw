package net.brokentrain.bandsaw.views;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.spi.LoggingEvent;

public class BandsawViewModelProvider {

    private static BandsawViewModelProvider content;
    private List<LoggingEvent> events;

    private BandsawViewModelProvider() {
        events = new ArrayList<LoggingEvent>();
    }

    public static synchronized BandsawViewModelProvider getInstance() {
        if (content != null) {
            return content;
        }
        content = new BandsawViewModelProvider();
        return content;
    }

    public List<LoggingEvent> getEvents() {
        return events;
    }

}

