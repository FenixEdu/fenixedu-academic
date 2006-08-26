package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.FileItemPermittedGroupType;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

/**
 * 
 * @author naat
 * 
 */
public class EditItemFilePermissions extends FileItemService {

    public void run(Integer itemId, Integer fileItemId, FileItemPermittedGroupType newPermittedGroupType)
            throws FenixServiceException, ExcepcaoPersistencia, DomainException, FileManagerException {

        final Item item = rootDomainObject.readItemByOID(itemId);
        final Group permittedGroup = createPermittedGroup(newPermittedGroupType, item.getSection().getSite().getExecutionCourse());

        final FileItem fileItem = FileItem.readByOID(fileItemId);
        fileItem.setFileItemPermittedGroupType(newPermittedGroupType);
        fileItem.setPermittedGroup(permittedGroup);

        FileManagerFactory.getFileManager().changeFilePermissions(
                fileItem.getExternalStorageIdentification(), (permittedGroup != null) ? true : false);

    }

}