package net.brokentrain.bandsaw.actions;


import net.brokentrain.bandsaw.BandsawUtilities;
import net.brokentrain.bandsaw.log4j.LogSet;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewActionDelegate;

/**
 * TODO: Provide description for "PauseAction".
 * @see IViewActionDelegate
 */
public class PauseAction implements IViewActionDelegate
{

    public static boolean sPaused;
    /**
     * TODO: Implement the "PauseAction" constructor.
     */
    public PauseAction()
    {
    }

    /**
     * TODO: Implement "run".
     * @see IViewActionDelegate#run
     */
    public void run(IAction action)
    {
        setPaused(!isPaused());
        if (!isPaused()) {
            LogSet.getInstance().revalidateAll();
            BandsawUtilities.resetTableRows();
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
    /**
     * @return
     */
    public static boolean isPaused()
    {
        return sPaused;
    }

    /**
     * @param aB
     */
    public static void setPaused(boolean aB)
    {
        sPaused = aB;
    }
}
