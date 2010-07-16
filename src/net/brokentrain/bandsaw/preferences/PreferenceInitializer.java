package net.brokentrain.bandsaw.preferences;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.log4j.ColumnList;
import net.brokentrain.bandsaw.log4j.Log4jItem;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Bandsaw.getDefault().getPreferenceStore();
        int[] defaultCols = { Log4jItem.LEVEL, Log4jItem.CATEGORY,
                Log4jItem.MESSAGE };
        store.setDefault(PreferenceConstants.P_COLUMNS,
                ColumnList.serialize(defaultCols));
    }

}
