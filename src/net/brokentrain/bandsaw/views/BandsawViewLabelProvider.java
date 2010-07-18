package net.brokentrain.bandsaw.views;

import java.util.Hashtable;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.log4j.Log4jItem;
import net.brokentrain.bandsaw.preferences.ColorPreferencePage;
import net.brokentrain.bandsaw.util.BandsawUtilities;
import net.brokentrain.bandsaw.util.PaintUtil;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class BandsawViewLabelProvider extends LabelProvider implements
        ITableLabelProvider, ITableColorProvider, ITableFontProvider {

    private static Hashtable<String, Color> colors = new Hashtable<String, Color>(
            5);

    public BandsawViewLabelProvider() {
        initColors();
    }

    public static void initColors() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
        colors.clear();
        colors.put(
                Level.DEBUG.toString(),
                new Color(Display.getCurrent(), PreferenceConverter.getColor(
                        store, ColorPreferencePage.DEBUG_COLOR_KEY)));
        colors.put(
                Level.INFO.toString(),
                new Color(Display.getCurrent(), PreferenceConverter.getColor(
                        store, ColorPreferencePage.INFO_COLOR_KEY)));
        colors.put(
                Level.WARN.toString(),
                new Color(Display.getCurrent(), PreferenceConverter.getColor(
                        store, ColorPreferencePage.WARN_COLOR_KEY)));
        colors.put(
                Level.ERROR.toString(),
                new Color(Display.getCurrent(), PreferenceConverter.getColor(
                        store, ColorPreferencePage.ERROR_COLOR_KEY)));
        colors.put(
                Level.FATAL.toString(),
                new Color(Display.getCurrent(), PreferenceConverter.getColor(
                        store, ColorPreferencePage.FATAL_COLOR_KEY)));
    }

    public String getColumnText(final Object obj, final int index) {
        LoggingEvent event = (LoggingEvent) obj;
        Log4jItem item = BandsawUtilities.Log4jItemFactory(index, event);
        return item.getText();
    }

    public Image getColumnImage(final Object obj, final int index) {
        if ((index == 0) && (obj instanceof LoggingEvent)) {
            LoggingEvent le = (LoggingEvent) obj;
            return PaintUtil.getIcon(le.getLevel().toInt());
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang
     * .Object, int)
     */
    public Color getBackground(Object arg0, int arg1) {
        return null;
        // LoggingEvent le = (LoggingEvent) arg0;
        // return colors.get(le.getLevel().toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang
     * .Object, int)
     */
    public Color getForeground(Object element, int columnIndex) {
        LoggingEvent le = (LoggingEvent) element;
        return colors.get(le.getLevel().toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ITableFontProvider#getFont(java.lang.Object,
     * int)
     */
    public Font getFont(Object element, int index) {
        if (index == Log4jItem.MESSAGE) {
            LoggingEvent le = (LoggingEvent) element;
            if (BandsawUtilities.isSQL(le.getRenderedMessage())) {
                return JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
            }
        }
       return null;
    }
}
