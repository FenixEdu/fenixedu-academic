package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import Dominio.IItem;
import Dominio.Item;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import fileSuport.FileSuport;
import fileSuport.IFileSuport;

/**
 * @author Fernanda Quitério
 *  
 */
public class DeleteItem implements IServico {
    private static DeleteItem service = new DeleteItem();

    public static DeleteItem getService() {
        return service;
    }

    private DeleteItem() {
    }

    public final String getNome() {
        return "DeleteItem";
    }

    public Boolean run(Integer infoExecutionCourseCode, Integer itemCode)
            throws FenixServiceException {
        try {

            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IPersistentItem persistentItem = persistentSuport
                    .getIPersistentItem();
            IItem deletedItem = (IItem) persistentItem.readByOID(Item.class,
                    itemCode);
            if (deletedItem == null) {
                return new Boolean(true);
            }
            IFileSuport fileSuport = FileSuport.getInstance();
            long size = 1;

            size = fileSuport.getDirectorySize(deletedItem.getSlideName());

            if (size > 0) {
                throw new notAuthorizedServiceDeleteException();
            }
            Integer orderOfDeletedItem = deletedItem.getItemOrder();
            persistentItem.delete(deletedItem);

            persistentSuport.confirmarTransaccao();
            persistentSuport.iniciarTransaccao();
            List itemsList = null;
            itemsList = persistentItem.readAllItemsBySection(deletedItem
                    .getSection());
            Iterator iterItems = itemsList.iterator();
            while (iterItems.hasNext()) {
                IItem item = (IItem) iterItems.next();
                Integer itemOrder = item.getItemOrder();
                if (itemOrder.intValue() > orderOfDeletedItem.intValue()) {
                    persistentItem.simpleLockWrite(item);
                    item.setItemOrder(new Integer(itemOrder.intValue() - 1));
                }
            }
            return new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}