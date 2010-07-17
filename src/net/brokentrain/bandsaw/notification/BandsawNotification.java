package net.brokentrain.bandsaw.notification;

import net.brokentrain.bandsaw.util.PaintUtil;

import org.eclipse.mylyn.internal.provisional.commons.ui.AbstractNotificationPopup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import org.apache.log4j.spi.LoggingEvent;

@SuppressWarnings("restriction")
public class BandsawNotification extends AbstractNotificationPopup {

    private LoggingEvent le;

    public BandsawNotification() {
        super(Display.getCurrent());
    }

    @Override
    protected void createContentArea(Composite composite) {
        composite.setLayout(new GridLayout(1, true));
        Label testLabel = new Label(composite, SWT.WRAP);
        testLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        testLabel.setText(this.le.getRenderedMessage());
        testLabel.setBackground(composite.getBackground());
    }

    @Override
    protected String getPopupShellTitle() {
        return "New logging event";
    }

    @Override
    protected Image getPopupShellImage(int maximumHeight) {
        return PaintUtil.getIcon(le.getLevel().toInt());
    }

    public void setLoggingEvent(LoggingEvent le) {
        this.le = le;
    }
}
