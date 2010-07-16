package net.brokentrain.bandsaw;

import net.brokentrain.bandsaw.util.PaintUtil;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class Bandsaw extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "net.brokentrain.bandsaw";

    private static Bandsaw plugin;

    public static Logger log = Logger.getLogger(Bandsaw.class);

    /**
     * The constructor.
     */
    public Bandsaw() {
    }

    /**
     * This method is called upon plug-in activation
     */
    @Override
    public final void start(final BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        PaintUtil.initIcons();
    }

    /**
     * This method is called when the plug-in is stopped
     */
    @Override
    public final void stop(final BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     */
    public static Bandsaw getDefault() {
        return plugin;
    }
}
