package ServidorAplicacao.Servico.gesdis.teacher;

/**
 *
 * @author  asnr e scpo
 */

import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoItem;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

	public Boolean run(InfoItem infoItem) throws FenixServiceException {
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentItem persistentItem =
				persistentSuport.getIPersistentItem();

			ISection section =
				Cloner.copyInfoSection2ISection(infoItem.getInfoSection());

			IItem deletedItem =
				persistentItem.readBySectionAndName(
					section,
					infoItem.getName());

			if (deletedItem == null)
				throw new FenixServiceException("non existing item");
				
			Integer orderOfDeletedItem = deletedItem.getItemOrder();

			persistentItem.delete(deletedItem);

			List itemsList = null;

			itemsList = persistentItem.readAllItemsBySection(section);

			Iterator iterItems = itemsList.iterator();

			while (iterItems.hasNext()) {

				IItem item = (IItem) iterItems.next();

				Integer itemOrder = item.getItemOrder();

				if (itemOrder.intValue() > orderOfDeletedItem.intValue()) {

					item.setItemOrder(new Integer(itemOrder.intValue() - 1));

					persistentItem.lockWrite(item);

				}

			}

			return new Boolean(true);

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
