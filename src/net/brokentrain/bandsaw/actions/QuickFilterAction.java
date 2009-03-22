package net.brokentrain.bandsaw.actions;


import net.brokentrain.bandsaw.BandsawUtilities;
import net.brokentrain.bandsaw.dialogs.QuickFilterDialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewActionDelegate;


/**
 * TODO: Provide description for "QuickFilterAction".
 * @see IViewActionDelegate
 */
public class QuickFilterAction implements IViewActionDelegate
{
    /**
     * TODO: Implement the "QuickFilterAction" constructor.
     */
    public QuickFilterAction()
    {
    }

    /**
     * TODO: Implement "run".
     * @see IViewActionDelegate#run
     */
    public void run(IAction action)
    {
        QuickFilterDialog dialog = new QuickFilterDialog(BandsawUtilities.getTable().getShell());
        dialog.open();
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
