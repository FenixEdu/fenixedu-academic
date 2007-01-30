package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

/**
 * Changes the group of people that is allowed to access the file.
 * 
 * @author naat
 */
public class EditItemFilePermissions extends FileItemService {

    public void run(Site site, FileItem fileItem, Group group)
            throws FenixServiceException, ExcepcaoPersistencia, DomainException, FileManagerException {

        fileItem.setPermittedGroup(group);
        FileManagerFactory.getFactoryInstance().getFileManager().changeFilePermissions(
                fileItem.getExternalStorageIdentification(), !isPublic(group));

    }

}