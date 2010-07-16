package net.brokentrain.bandsaw.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.php.internal.core.documentModel.provisional.contenttype.ContentTypeIdForPHP;

public class ModelUtil {

    public static boolean isPhpElement(final IModelElement modelElement) {
        Assert.isNotNull(modelElement);
        IModelElement sourceModule = modelElement
                .getAncestor(IModelElement.SOURCE_MODULE);
        if (sourceModule != null) {
            return isPhpFile((ISourceModule) sourceModule);
        }
        return false;
    }

    public static boolean isPhpFile(final ISourceModule sourceModule) {
        try {
            IResource resource = sourceModule.getCorrespondingResource();
            if (resource instanceof IFile) {
                IContentDescription contentDescription = ((IFile) resource)
                        .getContentDescription();
                return ContentTypeIdForPHP.ContentTypeID_PHP
                        .equals(contentDescription.getContentType().getId());
            }
        } catch (CoreException e) {
        }
        return hasPhpExtention(sourceModule.getElementName());
    }

    public static boolean isPhpFile(final IFile file) {
        IContentDescription contentDescription = null;
        if (!file.exists()) {
            return hasPhpExtention(file);
        }
        try {
            contentDescription = file.getContentDescription();
        } catch (final CoreException e) {
            return hasPhpExtention(file);
        }

        if (contentDescription == null) {
            return hasPhpExtention(file);
        }

        return ContentTypeIdForPHP.ContentTypeID_PHP.equals(contentDescription
                .getContentType().getId());
    }

    public static boolean hasPhpExtention(final IFile file) {
        final String fileName = file.getName();
        final int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return false;
        }
        String extension = fileName.substring(index + 1);
        final IContentType type = Platform.getContentTypeManager()
                .getContentType(ContentTypeIdForPHP.ContentTypeID_PHP);
        final String[] validExtensions = type
                .getFileSpecs(IContentType.FILE_EXTENSION_SPEC);
        for (String validExtension : validExtensions) {
            if (extension.equalsIgnoreCase(validExtension)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasPhpExtention(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException();
        }

        final int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return false;
        }
        String extension = fileName.substring(index + 1);

        final IContentType type = Platform.getContentTypeManager()
                .getContentType(ContentTypeIdForPHP.ContentTypeID_PHP);
        final String[] validExtensions = type
                .getFileSpecs(IContentType.FILE_EXTENSION_SPEC);
        for (String validExtension : validExtensions) {
            if (extension.equalsIgnoreCase(validExtension)) {
                return true;
            }
        }
        return false;
    }
}
