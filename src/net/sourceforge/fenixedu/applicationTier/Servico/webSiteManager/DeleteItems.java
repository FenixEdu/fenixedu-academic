package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Fernanda Quitério 26/09/2003
 *  
 */
public class DeleteItems implements IServico {

    private static DeleteItems service = new DeleteItems();

    public static DeleteItems getService() {

        return service;
    }

    private DeleteItems() {

    }

    public final String getNome() {

        return "DeleteItems";
    }

    //infoItem with an infoSection

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