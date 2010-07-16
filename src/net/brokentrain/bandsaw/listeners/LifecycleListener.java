package net.brokentrain.bandsaw.listeners;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.log4j.Log4jServer;
import net.brokentrain.bandsaw.preferences.Log4jPreferencePage;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public class LifecycleListener implements IPartListener {

    public void partActivated(IWorkbenchPart part) {
        boolean automatic = Bandsaw.getDefault().getPreferenceStore()
                .getBoolean(Log4jPreferencePage.P_AUTOMATIC);
        if (automatic) {
            Log4jServer.startListener();
        }
    }

    public void partBroughtToTop(IWorkbenchPart part) {
        // TODO Auto-generated method stub
    }

    public void partClosed(IWorkbenchPart part) {
        // boolean shutdown =
        // MessageDialog.openQuestion(
        // BandsawUtilities.getTable().getParent().getShell(),
        // "Shut Down Log4j Server",
        // "Do you want to stop listening for Log4j Messages?");
        // if (shutdown)
        // {
        // Log4jServer.stopListener();
        // }
    }

    public void partDeactivated(IWorkbenchPart part) {
        BandsawUtilities.saveTableColumnWidths();
    }

    public void partOpened(IWorkbenchPart part) {
        // TODO Auto-generated method stub
    }

}
