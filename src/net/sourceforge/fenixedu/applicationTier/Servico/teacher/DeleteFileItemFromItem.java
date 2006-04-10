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

    public void run(Integer itemID, Integer fileItemID)
            throws FenixServiceException, ExcepcaoPersistencia, DomainException {

        final Item item = rootDomainObject.readItemByOID(itemID);
        final FileItem fileItem = rootDomainObject.readFileItemByOID(fileItemID);
        
        item.removeFileItems(fileItem);

        try {
            DspaceClient.deleteFile(fileItem.getDspaceBitstreamIdentification());
        } catch (DspaceClientException e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

}
