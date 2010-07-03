package net.brokentrain.bandsaw;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import net.brokentrain.bandsaw.util.PaintUtil;

/**
 * The main plugin class to be used in the desktop.
 */
public class Bandsaw extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "net.brokentrain.bandsaw";

    private static Bandsaw plugin;

    private ResourceBundle resourceBundle;

    public static final int P_SERVER_TYPE_SOCKET_APPENDER = 1;

    public static final int P_SERVER_TYPE_SOCKET_HUB_APPENDER = 2;

    /**
     * The constructor.
     */
    public Bandsaw() {
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);

        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle("net.brokentrain.bandsaw.BandsawResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }

        PaintUtil.initIcons();
    }

    /**
     * This method is called when the plug-in is stopped
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     */
    public static Bandsaw getDefault() {
        return plugin;
    }

    /**
     * Returns the workspace instance.
     */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    /**
     * Returns the string from the plugin's resource bundle,
     * or 'key' if not found.
     */
    public static String getResourceString(String key) {
        ResourceBundle bundle = Bandsaw.getDefault().getResourceBundle();
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
     * Returns the plugin's resource bundle,
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

}
