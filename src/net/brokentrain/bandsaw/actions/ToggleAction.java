package net.brokentrain.bandsaw.actions;

import net.brokentrain.bandsaw.log4j.Log4jServer;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * @see IViewActionDelegate
 */
public class ToggleAction implements IViewActionDelegate {

    public ToggleAction() {
    }

    /**
     * @see IViewActionDelegate#run
     */
    public void run(IAction action) {
        if (action.isChecked()) {
            Log4jServer.stopListener();
            BandsawUtilities.updateStatus("Server stopped");
        } else {
            boolean success = Log4jServer.startListener();
            if (!success) {
                BandsawUtilities.showMessage("Could not start Log4j");
            } else {
                BandsawUtilities.updateStatus("Server started");
            }
        }
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
