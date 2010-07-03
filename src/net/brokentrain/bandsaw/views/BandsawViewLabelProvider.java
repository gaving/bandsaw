package net.brokentrain.bandsaw.views;

import net.brokentrain.bandsaw.util.BandsawUtilities;
import net.brokentrain.bandsaw.util.PaintUtil;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class BandsawViewLabelProvider extends LabelProvider implements ITableLabelProvider {

    public String getColumnText(final Object obj, final int index) {
        if (obj instanceof LoggingEvent) {
            LoggingEvent le = (LoggingEvent) obj;
            return BandsawUtilities.getColumnText(le, index);
        }
        return getText(obj);
    }

    public Image getColumnImage(final Object obj, final int index) {
        if ((index == 0) && (obj instanceof LoggingEvent)) {
            LoggingEvent le = (LoggingEvent) obj;
            return PaintUtil.getIcon(le.getLevel().toInt());
        }
        return null;
    }
}

