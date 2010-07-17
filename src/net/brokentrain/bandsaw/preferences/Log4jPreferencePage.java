package net.brokentrain.bandsaw.preferences;

import net.brokentrain.bandsaw.Bandsaw;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class Log4jPreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {

    /**
     * Port log4j server listens on
     */
    public static final String P_PORT = "log4j_socket_server_port";

    public static final String P_SERVER = "log4j_socket_server";

    public static final String P_AUTOMATIC = "log4j_automatic_start";

    public static final String P_SHOW_NOTIFICATIONS = "show_notifications";

    public RadioGroupFieldEditor radioEditor = null;

    public StringFieldEditor serverEditor = null;

    public Log4jPreferencePage() {
        super(GRID);
        setPreferenceStore(Bandsaw.getDefault().getPreferenceStore());
        setDescription("Log4j Settings");
        initializeDefaults();
    }

    /**
     * Sets the default values of the preferences.
     */
    public static void initializeDefaults() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
        store.setDefault(P_PORT, 4445);
        store.setDefault(P_AUTOMATIC, false);
        store.setDefault(P_SHOW_NOTIFICATIONS, false);
    }

    @Override
    public void createFieldEditors() {
        addField(new BooleanFieldEditor(P_AUTOMATIC, "&Automatic start",
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_SHOW_NOTIFICATIONS, "&Show notifications",
                getFieldEditorParent()));
    }

    /**
     * I needed to change the systems server type here, so I had to override
     * this.
     */
    @Override
    public boolean performOk() {
        return super.performOk();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
    }

    @Override
    protected void performApply() {
        // TODO Auto-generated method stub
        super.performApply();
    }

    public void init(IWorkbench workbench) {
    }

    public RadioGroupFieldEditor getRadioEditor() {
        return radioEditor;
    }

    public void setRadioEditor(RadioGroupFieldEditor radioEditor) {
        this.radioEditor = radioEditor;
    }

    public StringFieldEditor getServerEditor() {
        return serverEditor;
    }

    public void setServerEditor(StringFieldEditor serverEditor) {
        this.serverEditor = serverEditor;
    }
}
