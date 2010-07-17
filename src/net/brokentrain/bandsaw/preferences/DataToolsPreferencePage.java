package net.brokentrain.bandsaw.preferences;

import java.util.Arrays;

import net.brokentrain.bandsaw.Bandsaw;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class DataToolsPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage {

    private Combo profileList;
    private Label label;

    public static final String DEFAULT_DATA_PROFILE = "DEFAULT_DATA_PROFILE";

    public DataToolsPreferencePage() {
        setPreferenceStore(Bandsaw.getDefault().getPreferenceStore());
        setDescription("Data Tools Preferences");
        initializeDefaults();
    }

    public void init(IWorkbench workbench) {
    }

    public static void initializeDefaults() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
        store.setDefault(DataToolsPreferencePage.DEFAULT_DATA_PROFILE, "");
    }

    @Override
    protected Control createContents(Composite parent) {

        Composite entryTable = new Composite(parent, SWT.NULL);

        // Create a data that takes up the extra space in the dialog .
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        entryTable.setLayoutData(data);

        GridLayout layout = new GridLayout(2, false);
        entryTable.setLayout(layout);

        GridData gData;

        label = new Label(entryTable, SWT.NONE);
        label.setText("Default Profile");
        gData = new GridData(GridData.FILL_BOTH);
        gData.horizontalSpan = 2;
        label.setLayoutData(gData);

        // LIST
        profileList = new Combo(entryTable, SWT.DROP_DOWN | SWT.READ_ONLY);

        gData = new GridData();
        gData.horizontalSpan = 2;
        profileList.setLayoutData(gData);

        try {
            ProfileManager manager = ProfileManager.getInstance();
            IConnectionProfile[] availableProfiles = manager.getProfiles();

            if (availableProfiles.length > 0) {
                for (IConnectionProfile profile : availableProfiles) {
                    profileList.add(profile.getName());
                }

                String[] items = profileList.getItems();
                Arrays.sort(items);
                profileList.setItems(items);
                profileList.select(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void storeValues() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
        store.setValue(DEFAULT_DATA_PROFILE, profileList.getText());
    }


    @Override
    public boolean performOk() {
        boolean ok = super.performOk();
        storeValues();
        return ok;
    }

    @Override
    protected void performApply() {
        storeValues();
        super.performApply();
    }

}
