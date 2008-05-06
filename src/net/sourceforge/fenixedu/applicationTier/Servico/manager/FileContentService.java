package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;

/**
 * 
 * @author naat
 * 
 */
public abstract class FileContentService extends Service {

    protected boolean isPublic(Group permittedGroup) {
        if (permittedGroup == null) {
            return true;
        }
        
        if (permittedGroup instanceof EveryoneGroup) {
            return true;
        }
        
        return false;
    }

    protected FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean permission,
	    Collection<FileSetMetaData> metaData, File file) throws FenixServiceException, IOException {
	final IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();
	InputStream is = null;
	try {
	    is = new FileInputStream(file);
	    return fileManager.saveFile(filePath, originalFilename, permission, metaData, is);
	} catch (FileNotFoundException e) {
	    throw new FenixServiceException(e.getMessage());
	} finally {
	    if (is != null) {
		is.close();
	    }
	}
    }
}