package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.io.InputStream;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetType;
import pt.utl.ist.fenix.tools.file.IFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;



public class CreateScormPackageForItem extends CreateFileItemForItem {

	public void run(Item item, InputStream inputStream, String originalFilename,
            String displayName, Group permittedGroup) throws DomainException, FenixServiceException, ExcepcaoPersistencia {
		
       super.run(item, inputStream, originalFilename, displayName, permittedGroup);
	}
	
	@Override
	protected FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean permission, String name, String displayName, InputStream inputStream) {
    	final IFileManager fileManager = FileManagerFactory.getFileManager();
    	
    	return  fileManager.saveScormFile(filePath, originalFilename,permission, name, displayName, inputStream,FileSetType.PACKAGE_SCORM_1_2);
    	
	}
	
}
