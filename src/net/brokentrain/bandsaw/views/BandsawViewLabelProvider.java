package net.brokentrain.bandsaw.views;

import net.brokentrain.bandsaw.log4j.Log4jItem;
import net.brokentrain.bandsaw.util.BandsawUtilities;
import net.brokentrain.bandsaw.util.PaintUtil;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class BandsawViewLabelProvider extends LabelProvider implements ITableLabelProvider {

    public String getColumnText(final Object obj, final int index) {
        LoggingEvent event = (LoggingEvent) obj;
        Log4jItem item = BandsawUtilities.Log4jItemFactory(index, event);
        return item.getText();
    }

    public Image getColumnImage(final Object obj, final int index) {
        if ((index == 0) && (obj instanceof LoggingEvent)) {
            LoggingEvent le = (LoggingEvent) obj;
            return PaintUtil.getIcon(le.getLevel().toInt());
        }
        return null;
    }
}

