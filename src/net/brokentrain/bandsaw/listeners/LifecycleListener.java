package net.brokentrain.bandsaw.listeners;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.log4j.Log4jServer;
import net.brokentrain.bandsaw.preferences.Log4jPreferencePage;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public class LifecycleListener implements IPartListener {

    public void partActivated(IWorkbenchPart part) {
        boolean automatic = Bandsaw.getDefault().getPreferenceStore()
                .getBoolean(Log4jPreferencePage.P_AUTOMATIC);
        if (automatic) {
            boolean success = Log4jServer.startListener();
            if (success) {
                /* TODO: Show host and port */
                BandsawUtilities.updateStatus("Server started");

                IContributionItem item = BandsawUtilities.getSite()
                    .getActionBars().getToolBarManager()
                               .find("Bandsaw.ToggleAction");
                ((ActionContributionItem) item).getAction().setChecked(true);
//                ((PluginAction) ((ActionContributionItem) item).getAction()).g
            }
        }
    }

    public void partBroughtToTop(IWorkbenchPart part) {
        // TODO Auto-generated method stub
    }

    public void partClosed(IWorkbenchPart part) {
        // boolean shutdown =
            // MessageDialog.openQuestion(
                    // BandsawUtilities.getTable().getParent().getShell(),
                    // "Stop listening",
                    // "Do you want to stop listening for log4j events?");
        // if (shutdown) {
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
