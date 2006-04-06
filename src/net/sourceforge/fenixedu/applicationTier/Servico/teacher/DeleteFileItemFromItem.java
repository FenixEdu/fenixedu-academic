package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.integrationTier.dspace.DspaceClient;
import net.sourceforge.fenixedu.integrationTier.dspace.DspaceClientException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author naat
 * 
 */
public class DeleteFileItemFromItem extends Service {

    public void run(Integer itemId, Integer fileItemId)
            throws FenixServiceException, ExcepcaoPersistencia, DomainException {

        Item item = rootDomainObject.readItemByOID(itemId);
        FileItem fileItem = rootDomainObject.readFileItemByOID(fileItemId);
        item.removeFileItems(fileItem);

        try {
            DspaceClient.deleteFile(fileItem.getDspaceBitstreamIdentification());
        } catch (DspaceClientException e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

}
