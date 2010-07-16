package net.brokentrain.bandsaw.actions;

import java.util.List;

import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;

public class CopyAction extends Action {

    public CopyAction(String label) {
        super(label);
    }

    /**
     * @param args
     */
    @Override
    public void run() {
        @SuppressWarnings("rawtypes")
        List leList = ((IStructuredSelection) BandsawUtilities.getViewer()
                .getSelection()).toList();

        if (leList.isEmpty()) {
            return;
        }

        String renderedMessage = "";

        Clipboard clipboard = new Clipboard(BandsawUtilities.getViewer()
                .getControl().getDisplay());
        for (Object obj : leList) {
            LoggingEvent le = (LoggingEvent) obj;
            renderedMessage += le.getRenderedMessage();
            clipboard.setContents(new Object[] { renderedMessage },
                    new Transfer[] { TextTransfer.getInstance() });
        }

        IActionBars bars = ((IViewSite) BandsawUtilities.getViewer().getInput())
                .getActionBars();
        bars.getStatusLineManager().setMessage(
                "Copied " + renderedMessage.length()
                        + " characters to clipboard");
        clipboard.dispose();
    }
}
