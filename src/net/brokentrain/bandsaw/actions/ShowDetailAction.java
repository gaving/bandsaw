package net.brokentrain.bandsaw.actions;

import java.util.List;

import net.brokentrain.bandsaw.dialogs.Log4jEventDialog;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

public class ShowDetailAction extends Action {

    public void run() {

        /* Viewer from the view */
        TableViewer viewer = BandsawUtilities.getViewer();

        /* Retrieve all the currently highlighted items of the viewer */
        List leList = ((IStructuredSelection)viewer.getSelection()).toList();
        for (Object obj : leList) {

            /* Pass the LoggingEvent from the model over to the dialog */
            LoggingEvent le = (LoggingEvent) obj;
            Log4jEventDialog dialog = new Log4jEventDialog(viewer.getControl().getParent().getShell(), le);
            dialog.open();
        }
    }

}
