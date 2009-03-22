package net.brokentrain.bandsaw.actions;


import net.brokentrain.bandsaw.BandsawUtilities;
import net.brokentrain.bandsaw.dialogs.Log4jEventDialog;
import net.brokentrain.bandsaw.log4j.LogSet;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.action.Action;


/**
 * @author Brandon
 */
public class ShowDetailAction extends Action
{
    public void run()
    {
        LoggingEvent le = LogSet.getInstance().getLoggingEventShowingAt( BandsawUtilities.getTable().getSelectionIndex());
        Log4jEventDialog dialog = new Log4jEventDialog(BandsawUtilities.getTable().getParent().getShell(), le);
        dialog.open();
    }

}
