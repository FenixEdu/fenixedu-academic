/*
 * Created on 31/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoItem;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac2
 */
public class EditItem implements IServico {

	private static EditItem service = new EditItem();

	/**
	
	 * The singleton access method of this class.
	
	 **/

	public static EditItem getService() {

		return service;

	}

	public String getNome() {

		return "EditItem";

	}

	// this method reorders some items but not the item that we are editing 
	private void organizeItemsOrder(
		int newOrder,
		int oldOrder,
		ISection section)
		throws FenixServiceException {

		IPersistentItem persistentItem = null;
		try {

			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			persistentItem = persistentSuport.getIPersistentItem();

			List itemsList = null;

			itemsList = persistentItem.readAllItemsBySection(section);

			Iterator iterItems = itemsList.iterator();

			if (newOrder - oldOrder > 0)
				while (iterItems.hasNext()) {

					IItem iterItem = (IItem) iterItems.next();

					int iterItemOrder = iterItem.getItemOrder().intValue();

					if (iterItemOrder > oldOrder
						&& iterItemOrder <= newOrder) {

						iterItem.setItemOrder(new Integer(iterItemOrder - 1));

						persistentItem.lockWrite(iterItem);
					}
				} else
				while (iterItems.hasNext()) {

					IItem iterItem = (IItem) iterItems.next();

					int iterItemOrder = iterItem.getItemOrder().intValue();

					if (iterItemOrder >= newOrder
						&& iterItemOrder < oldOrder) {

						iterItem.setItemOrder(new Integer(iterItemOrder + 1));

						persistentItem.lockWrite(iterItem);

					}
				}
		} catch (ExistingPersistentException excepcaoPersistencia) {

		throw new ExistingServiceException(excepcaoPersistencia);
	}
		
		catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}
	}

	/**
	 * Executes the service.
	 *
	 **/
	public Boolean run(InfoItem oldInfoItem, InfoItem newInfoItem)
		throws FenixServiceException {

		ISection fatherSection = null;
		IItem item = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentSection persistentSection = sp.getIPersistentSection();
			IPersistentItem persistentItem = sp.getIPersistentItem();

			ISection section =
				Cloner.copyInfoSection2ISection(oldInfoItem.getInfoSection());

			item =
				persistentItem.readBySectionAndName(
					section,
					oldInfoItem.getName());

			item.setInformation(newInfoItem.getInformation());
			int newOrder = newInfoItem.getItemOrder().intValue();
			int oldOrder = oldInfoItem.getItemOrder().intValue();

			if (newOrder != oldOrder) {
				organizeItemsOrder(newOrder, oldOrder, section);
				item.setItemOrder(newInfoItem.getItemOrder());
			}
			item.setName(newInfoItem.getName());
			item.setUrgent(newInfoItem.getUrgent());
			persistentItem.lockWrite(item);
		} catch (ExistingPersistentException e) {

			throw new ExistingServiceException(e);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return new Boolean(true);
	}

}