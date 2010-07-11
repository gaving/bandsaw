package net.brokentrain.bandsaw.actions;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import net.brokentrain.bandsaw.util.BandsawUtilities;
import net.brokentrain.bandsaw.util.FileUtil;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class JumpAction extends Action {

    public JumpAction(String label) {
        super(label);
    }

    public void run() {

        @SuppressWarnings("rawtypes")
            List leList = ((IStructuredSelection)BandsawUtilities.getViewer().getSelection()).toList();

        if (leList.isEmpty()) {
            return;
        }

        for (Object obj : leList) {
            LoggingEvent le = (LoggingEvent) obj;

            if (le.locationInformationExists()) {

                LocationInfo locationInfo = le.getLocationInformation();

                //String className = locationInfo.getClassName();
                String fileName = locationInfo.getFileName();
                String lineNumber = locationInfo.getLineNumber();
                String message = le.getRenderedMessage();

                // System.out.println(className);
                // System.out.println(fileName);
                // System.out.println(lineNumber);

                File fileToOpen = new File(fileName);
                if (!fileToOpen.exists() || !fileToOpen.isFile()) {
                    return;
                }

                String fileExtension = FileUtil.getExtension(fileToOpen);

                System.out.println(fileExtension);

                if (fileExtension.equals("php")) {

                    System.out.println("Handling it like a php file");

                    IResource resource = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(fileToOpen.getAbsolutePath()));
                    ISourceModule model = (ISourceModule) DLTKCore.create((IFile) resource);
                    if (resource == null || !(resource instanceof IFile)) {

                        System.out.println("Can't find the file in the workspace - just open a normal editor");
                        FileUtil.openFile(fileToOpen);
                        return;
                    }

                    try {

                        HashMap<String, Integer> map = new HashMap<String, Integer>();
                        MarkerUtilities.setMessage(map, message);
                        MarkerUtilities.setLineNumber(map, Integer.valueOf(lineNumber));
                        MarkerUtilities.createMarker((IFile)resource, map, IMarker.BOOKMARK);

                        /* Set a marker with the text mouse overed! */

                        ModuleDeclaration moduleDeclaration = SourceParserUtil.getModuleDeclaration((org.eclipse.dltk.core.ISourceModule) model, null);
                        moduleDeclaration.traverse(new ASTVisitor() {

                            public boolean visit(Expression s) throws Exception {
                                System.out.println(s.toString());
                                return super.visit(s);
                            }

                            public boolean visit(Statement s) throws Exception {
                                System.out.println(s.toString());
                                return super.visit((Statement) s);
                            };
                        });

                        /* Jump to class or method, parse that ast tree? */
                        IEditorPart editor = DLTKUIPlugin.openInEditor(model, true, true);
                        FileUtil.selectEclipseEditorRegion(editor, Integer.valueOf(lineNumber), 0, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    System.out.println("Handling it like another file");

                    FileUtil.openFile(fileToOpen);
                }
            }
        }
    }

    /*
     *     private void openTypes() {
     *         IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
     *         root.getProject
     *
     *             Shell parent = DLTKUIPlugin.getActiveWorkbenchShell();
     *         OpenTypeSelectionDialog2 dialog = new OpenTypeSelectionDialog2(parent,
     *                 true, PlatformUI.getWorkbench().getProgressService(), null,
     *                 IDLTKSearchConstants.TYPE, PHPUILanguageToolkit.getInstance());
     *         dialog.setTitle("what");
     *         dialog.setMessage("yeah");
     *
     *         int result = dialog.open();
     *         if (result != IDialogConstants.OK_ID)
     *             return;
     *
     *         PHPUILanguageToolkit.getInstance().g
     *
     *             OpenTypeHistory history = OpenTypeHistory.getInstance(PHPUILanguageToolkit.getInstance());
     *         List result = new ArrayList(selected.length);
     *         for (int i = 0; i < selected.length; i++) {
     *             TypeNameMatch typeInfo = selected[i];
     *             IType type = typeInfo.getType();
     *             if (!type.exists()) {
     *                 ISourceModule module = type.getSourceModule();
     *                 if (module.exists()) {
     *                     result.add(module);
     *                 } else {
     *                     String title = DLTKUIMessages.TypeSelectionDialog_errorTitle;
     *                     IProjectFragment root = typeInfo.getProjectFragment();
     *                     ScriptElementLabels labels = this.fToolkit
     *                         .getScriptElementLabels();
     *                     String containerName = labels.getElementLabel(root,
     *                             ScriptElementLabels.ROOT_QUALIFIED);
     *                     String message = Messages.format(
     *                             DLTKUIMessages.TypeSelectionDialog_dialogMessage,
     *                             new String[] { typeInfo.getFullyQualifiedName(),
     *                                 containerName });
     *                     MessageDialog.openError(getShell(), title, message);
     *                     history.remove(typeInfo);
     *                     setResult(null);
     *                 }
     *             } else {
     *                 history.accessed(typeInfo);
     *                 result.add(type);
     *             }
     *         }
     *
     *         type = (IModelElement) types[i];
     *         DLTKUIPlugin.openInEditor(type, true, true);
     *     }
     */

}
