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
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSite;
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

			ISection section =
				Cloner.copyInfoSection2ISection(infoItem.getInfoSection());

			ISite site = Cloner.copyInfoSite2ISite(infoItem.getInfoSection().getInfoSite());
			IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
			site =
				persistentSite.readByExecutionCourse(site.getExecutionCourse());
		
			section.setSite(site);
			
			IPersistentItem persistentItem =
										persistentSuport.getIPersistentItem();
			
			IItem deletedItem =
				persistentItem.readBySectionAndName(
					section,
					infoItem.getName());

			if (deletedItem == null)
				{
					return new Boolean(true);
				}
				
			Integer orderOfDeletedItem = deletedItem.getItemOrder();

			persistentItem.delete(deletedItem);
			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();
			
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
