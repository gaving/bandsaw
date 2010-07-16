package net.brokentrain.bandsaw.views;

import java.util.List;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class BandsawViewContentProvider implements IStructuredContentProvider {

    public void inputChanged(final Viewer v, final Object oldInput,
            final Object newInput) {
    }

    public void dispose() {
    }

    public final Object[] getElements(final Object inputElement) {
        @SuppressWarnings("unchecked")
        List<LoggingEvent> events = (List<LoggingEvent>) inputElement;
        return events.toArray();
    }
}
