package ServidorAplicacao.Servico.gesdis.teacher;

/**
 *
 * @author  asnr e scpo
 */

import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
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

	public Boolean run(InfoSection infoSection, String itemName)
		throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			
			
			IPersistentSection persistentSection = persistentSuport.getIPersistentSection();
			
			IPersistentItem persistentItem = persistentSuport.getIPersistentItem();
		
			ISite site = Cloner.copyInfoSite2ISite(infoSection.getInfoSite());
	 		
			ISection section = persistentSection.readBySiteAndSectionAndName(site,null,infoSection.getName());
						
			
			IItem deletedItem = persistentItem.readBySectionAndName(section, itemName);
			
			Integer orderOfDeletedItem = deletedItem.getItemOrder();
			
			if (deletedItem == null) throw new FenixServiceException("non existing item");
			//	throw new ItemInexistenteException("Item inexistente");
			
			persistentItem.delete(deletedItem);
			
			List itemsList =null;
			
			itemsList = section.getItems();
			
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
