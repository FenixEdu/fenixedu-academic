package ServidorAplicacao.Servico.webSiteManager;

import java.util.Iterator;
import java.util.List;

import Dominio.IWebSiteItem;
import Dominio.WebSiteItem;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSiteItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
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