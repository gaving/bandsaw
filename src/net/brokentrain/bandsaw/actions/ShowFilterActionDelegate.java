package net.brokentrain.bandsaw.actions;

import net.brokentrain.bandsaw.util.BandsawUtilities;
import net.brokentrain.bandsaw.views.BandsawView;
import net.brokentrain.bandsaw.views.BandsawViewFilter;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class ShowFilterActionDelegate implements IViewActionDelegate {

    private boolean shouldFilter = false;
    private ViewerFilter filter;
    private BandsawView view;
    private TableViewer viewer;

    public ShowFilterActionDelegate() {
        this.filter = new BandsawViewFilter();
    }

    public void init(IViewPart view) {
        this.view = (BandsawView) view;
        this.viewer = BandsawUtilities.getViewer();
    }

    public void run(IAction action) {
        if (!shouldFilter) {
            shouldFilter = true;
            viewer.addFilter(filter);
        } else {
            shouldFilter = false;
            ViewerFilter[] filters = viewer.getFilters();
            for (ViewerFilter filter : filters) {
                viewer.removeFilter(filter);
            }
        }
        view.toggleFilter();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

}
