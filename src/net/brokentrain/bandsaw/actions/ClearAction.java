package net.brokentrain.bandsaw.actions;

import java.util.List;

import net.brokentrain.bandsaw.util.BandsawUtilities;
import net.brokentrain.bandsaw.views.BandsawViewModelProvider;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * @see IViewActionDelegate
 */
public class ClearAction implements IViewActionDelegate {

    public ClearAction() {
    }

    /**
     * @see IViewActionDelegate#run
     */
    public void run(IAction action) {
        List<LoggingEvent> events = BandsawViewModelProvider.getInstance().getEvents();
        events.clear();
        BandsawUtilities.getViewer().refresh();
    }

    /**
     * @see IViewActionDelegate#selectionChanged
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
     * @see IViewActionDelegate#init
     */
    public void init(IViewPart view) {
    }
}
