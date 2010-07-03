package net.brokentrain.bandsaw.actions;


import net.brokentrain.bandsaw.log4j.Log4jServer;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewActionDelegate;


/**
 * TODO: Provide description for "StartAction".
 * @see IViewActionDelegate
 */
public class StartAction implements IViewActionDelegate
{
    /**
     * TODO: Implement the "StartAction" constructor.
     */
    public StartAction()
    {
    }

    /**
     * TODO: Implement "run".
     * @see IViewActionDelegate#run
     */
    public void run(IAction action)
    {
        boolean success = Log4jServer.startListener();
        if (success) {
            action.setEnabled(false);
        } else {
            BandsawUtilities.showMessage("Could not start Log4j");
        }
    }

    /**
     * TODO: Implement "selectionChanged".
     * @see IViewActionDelegate#selectionChanged
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
    }

    /**
     * TODO: Implement "init".
     * @see IViewActionDelegate#init
     */
    public void init(IViewPart view)
    {
    }
}
