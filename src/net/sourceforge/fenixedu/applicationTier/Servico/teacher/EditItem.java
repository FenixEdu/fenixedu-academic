package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 *  
 */
public class EditItem implements IService {

    /**
     * Executes the service.
     *  
     */
    public Boolean run(Integer infoExecutionCourseCode, Integer itemCode, InfoItem newInfoItem)
            throws FenixServiceException {
        IItem item = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentItem persistentItem = sp.getIPersistentItem();
            
            item = (IItem) persistentItem.readByOID(Item.class, itemCode);

            if (item == null) {
                throw new ExistingServiceException();
            }
                     
            persistentItem.simpleLockWrite(item);
            item.editItem(newInfoItem.getName(), newInfoItem.getInformation(), newInfoItem.getUrgent(), newInfoItem.getItemOrder());

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return new Boolean(true);
    }

}