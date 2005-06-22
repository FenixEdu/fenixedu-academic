package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 26/09/2003
 *  
 */
public class DeleteItems implements IService {

    public boolean run(Integer sectionCode, List itemsToDelete) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentWebSiteItem persistentWebSiteItem = persistentSuport.getIPersistentWebSiteItem();

            Iterator iterItemsCode = itemsToDelete.iterator();
            while (iterItemsCode.hasNext()) {
                Integer itemCode = (Integer) iterItemsCode.next();

                IWebSiteItem webSiteItemAux = (IWebSiteItem) persistentWebSiteItem.readByOID(
                        WebSiteItem.class, itemCode, true);
                if (webSiteItemAux != null) {
                    persistentWebSiteItem.delete(webSiteItemAux);
                }
            }
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        return true;
    }
}