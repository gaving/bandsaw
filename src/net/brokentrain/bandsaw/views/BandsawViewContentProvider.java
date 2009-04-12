package net.brokentrain.bandsaw.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class BandsawViewContentProvider implements IStructuredContentProvider {

    public void inputChanged(final Viewer v, final Object oldInput, final Object newInput) {
    }

    public void dispose() {
    }

    public Object[] getElements(final Object parent) {
        return new String[] {};
    }
}

