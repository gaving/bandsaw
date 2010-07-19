package net.brokentrain.bandsaw.actions;

import java.util.HashMap;
import java.util.List;

import net.brokentrain.bandsaw.Bandsaw;
import net.brokentrain.bandsaw.preferences.DataToolsPreferencePage;
import net.brokentrain.bandsaw.util.BandsawUtilities;
import net.brokentrain.bandsaw.util.MessageBoxUtil;

import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.sqltools.core.DatabaseIdentifier;
import org.eclipse.datatools.sqltools.sqleditor.EditorConstants;
import org.eclipse.datatools.sqltools.sqleditor.internal.actions.Messages;
import org.eclipse.datatools.sqltools.sqleditor.result.GroupSQLResultRunnable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;

public class ExecuteQueryAction extends Action {

    public ExecuteQueryAction(final String label) {
        super(label);
    }

    @Override
        public final void run() {

            @SuppressWarnings("rawtypes")
                List leList = ((IStructuredSelection) BandsawUtilities.getViewer()
                        .getSelection()).toList();

            if (leList.isEmpty()) {
                return;
            }

            for (Object obj : leList) {
                LoggingEvent le = (LoggingEvent) obj;
                String message = le.getRenderedMessage();
                executeQuery(message);
            }
        }

    private void executeQuery(String query) {
        try {

            String defaultProfile = Bandsaw.getDefault().getPreferenceStore()
                .getString(DataToolsPreferencePage.DEFAULT_DATA_PROFILE);
            if ((defaultProfile != null) && (!defaultProfile.isEmpty())) {

                IConnectionProfile profile =
                    ProfileManager.getInstance().getProfileByName(defaultProfile);
                IStatus status = profile.connect();
                if (status.getCode() == IStatus.OK) {
                    executeDDL(query, profile);
                } else {
                    if (status.getException() != null) {
                        status.getException().printStackTrace();
                    }
                }
            } else {
                MessageBoxUtil
                .showMessage(BandsawUtilities.getSite().getShell(), SWT.OK | SWT.ICON_INFORMATION,
                        "Error",
                        "Could not find a default profile to use");                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeDDL(String statement,
            IConnectionProfile connectionProfile) {
        DatabaseIdentifier databaseIdentifier = new DatabaseIdentifier(
                connectionProfile.getName(), "");
        if (databaseIdentifier != null) {
            try {
                Job job = new GroupSQLResultRunnable(null, new String[] { statement }, null,
                        null, databaseIdentifier, false, new HashMap<Object, Object>(),
                        Messages.BaseExecuteAction_group_exec_title,
                        "Generate DDL");
                job.setName(Messages.BaseExecuteAction_job_title);
                job.setUser(true);
                job.schedule();

                PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getActivePage().showView(EditorConstants.RESULTS_VIEW);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
