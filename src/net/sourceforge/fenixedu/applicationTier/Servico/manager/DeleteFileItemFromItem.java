package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.fenix.tools.file.FileManagerException;

/**
 * @author naat
 */
public class DeleteFileItemFromItem extends Service {

    public void run(Site site, FileItem fileItem) throws FenixServiceException,
            ExcepcaoPersistencia, DomainException, FileManagerException {

        fileItem.delete();

        new DeleteFileRequest(AccessControl.getPerson(),fileItem.getExternalStorageIdentification());

//        final IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();
//        fileManager.deleteFile(fileItem.getExternalStorageIdentification());
    }
    
}
