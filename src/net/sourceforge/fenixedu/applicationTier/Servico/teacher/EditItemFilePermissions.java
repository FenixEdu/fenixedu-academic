package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.FileItemService;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.FileItemPermittedGroupType;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.integrationTier.dspace.DspaceClient;
import net.sourceforge.fenixedu.integrationTier.dspace.DspaceClientException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author naat
 * 
 */
public class EditItemFilePermissions extends FileItemService {

    public void run(Integer itemId, Integer fileItemId,
            FileItemPermittedGroupType newPermittedGroupType) throws FenixServiceException,
            ExcepcaoPersistencia, DomainException {

        final Item item = rootDomainObject.readItemByOID(itemId);
        final FileItem fileItem = rootDomainObject.readFileItemByOID(fileItemId);

        final Group permittedGroup = createPermittedGroup(newPermittedGroupType, item.getSection()
                .getSite().getExecutionCourse());

        try {
            DspaceClient.changeFilePermissions(fileItem.getDspaceBitstreamIdentification(),
                    (permittedGroup != null) ? true : false);
        } catch (DspaceClientException e) {
            throw new FenixServiceException(e.getMessage(), e);
        }

        fileItem.setPermittedGroupType(newPermittedGroupType);
        fileItem.setPermittedGroup(permittedGroup);
        
    }

}