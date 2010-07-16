package net.brokentrain.bandsaw.preferences;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.util.BandsawUtilities;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ColorPreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {

    public ColorPreferencePage() {
        super(GRID);
        setPreferenceStore(Bandsaw.getDefault().getPreferenceStore());
        setDescription("Color Preferences");
        initializeDefaults();
    }

    public static final String DEBUG_COLOR_KEY = "DEBUG_COLOR_KEY";
    public static final String INFO_COLOR_KEY = "INFO_COLOR_KEY";
    public static final String WARN_COLOR_KEY = "WARN_COLOR_KEY";
    public static final String ERROR_COLOR_KEY = "ERROR_COLOR_KEY";
    public static final String FATAL_COLOR_KEY = "FATAL_COLOR_KEY";

    public void init(IWorkbench workbench) {
    }

    public static void initializeDefaults() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
        PreferenceConverter.setDefault(store,
                ColorPreferencePage.DEBUG_COLOR_KEY, new RGB(0, 0, 0));
        PreferenceConverter.setDefault(store,
                ColorPreferencePage.INFO_COLOR_KEY, new RGB(0, 255, 0));
        PreferenceConverter.setDefault(store,
                ColorPreferencePage.WARN_COLOR_KEY, new RGB(255, 128, 0));
        PreferenceConverter.setDefault(store,
                ColorPreferencePage.ERROR_COLOR_KEY, new RGB(255, 0, 0));
        PreferenceConverter.setDefault(store,
                ColorPreferencePage.FATAL_COLOR_KEY, new RGB(255, 0, 0));
    }

    @Override
    public void createFieldEditors() {

        addField(new ColorFieldEditor(DEBUG_COLOR_KEY, "Debug",
                getFieldEditorParent()));

        addField(new ColorFieldEditor(INFO_COLOR_KEY, "Info",
                getFieldEditorParent()));

        addField(new ColorFieldEditor(WARN_COLOR_KEY, "Warn",
                getFieldEditorParent()));

        addField(new ColorFieldEditor(ERROR_COLOR_KEY, "Error",
                getFieldEditorParent()));

        addField(new ColorFieldEditor(FATAL_COLOR_KEY, "Fatal",
                getFieldEditorParent()));
    }

    @Override
    public boolean performOk() {
        boolean ok = super.performOk();
        BandsawUtilities.updateColors();
        return ok;
    }

    @Override
    protected void performApply() {
        super.performApply();
        BandsawUtilities.updateColors();
    }

}
