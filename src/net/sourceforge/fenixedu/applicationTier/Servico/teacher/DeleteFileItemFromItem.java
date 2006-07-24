package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;

/**
 * 
 * @author naat
 * 
 */
public class DeleteFileItemFromItem extends Service {

    public void run(Integer itemId, Integer fileItemId) throws FenixServiceException,
            ExcepcaoPersistencia, DomainException, FileManagerException {

        final Item item = rootDomainObject.readItemByOID(itemId);
        final FileItem fileItem = FileItem.readByOID(fileItemId);

        item.removeFileItems(fileItem);

        final IFileManager fileManager = FileManagerFactory.getFileManager();
        fileManager.deleteFile(fileItem.getExternalStorageIdentification());
    }
}
