package net.brokentrain.bandsaw.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * TODO: Provide description for "LockAction".
 * 
 * @see IViewActionDelegate
 */
public class LockAction implements IViewActionDelegate {

    public static boolean sLocked;

    /**
     * TODO: Implement the "LockAction" constructor.
     */
    public LockAction() {
    }

    /**
     * TODO: Implement "run".
     * 
     * @see IViewActionDelegate#run
     */
    public void run(IAction action) {
        setLocked(!isLocked());
    }

    /**
     * TODO: Implement "selectionChanged".
     * 
     * @see IViewActionDelegate#selectionChanged
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
     * TODO: Implement "init".
     * 
     * @see IViewActionDelegate#init
     */
    public void init(IViewPart view) {
    }

    /**
     * @return
     */
    public static boolean isLocked() {
        return sLocked;
    }

    /**
     * @param aB
     */
    public static void setLocked(boolean aB) {
        sLocked = aB;
    }
}
