/*
 * Created on 13/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
 
 
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.gesdis.InfoItem;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Item;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


/**
 * @author asnr e scpo
 */

public class InsertItem implements IServico {

	
	private static InsertItem service = new InsertItem();

	
	public static InsertItem getService() {

		return service;	
	}

	
	private InsertItem() {
	
	}


	public final String getNome() {

		return "InsertItem";
	}
	
	
	private void organizeExistingItemsOrder(ISection section, int insertItemOrder)
	throws FenixServiceException {
	
		IPersistentItem persistentItem=null;
		try{
		
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();	
			persistentItem = persistentSuport.getIPersistentItem();
		
			List itemsList = persistentItem.readAllItemsBySection(section);
	
			if (itemsList!= null) {
			
				Iterator iterItems = itemsList.iterator();
				while (iterItems.hasNext()) {

					IItem item = (IItem) iterItems.next();
					int itemOrder = item.getItemOrder().intValue();

					if (itemOrder >= insertItemOrder) {
						item.setItemOrder(new Integer(itemOrder+1));
						persistentItem.lockWrite(item);
					}
		   			
		   		}

			}
		}
		catch (ExcepcaoPersistencia excepcaoPersistencia){
	
			throw new FenixServiceException(excepcaoPersistencia);
			}
	}
	
	
	//infoItem with an infoSection
	
	public Boolean run(InfoItem infoItem) throws FenixServiceException {

		IItem item = null;
		ISection section = null;

		try {

			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();

			IPersistentItem persistentItem =
				persistentSuport.getIPersistentItem();

			section =
				Cloner.copyInfoSection2ISection(infoItem.getInfoSection());
			ISite site = section.getSite();
			site = persistentSuport.getIPersistentSite().readByExecutionCourse(site.getExecutionCourse());
			section.setSite(site);
	
			IItem existingItem =
				persistentItem.readBySectionAndName(
					section,
					infoItem.getName());
					
			if (existingItem != null)
				throw new ExistingServiceException();

		    item = new Item(infoItem.getName(), section, infoItem.getItemOrder(), infoItem.getInformation(), infoItem.getUrgent()); 
			
			organizeExistingItemsOrder(
				section,
				infoItem.getItemOrder().intValue());

			persistentItem.lockWrite(item);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}

		return new Boolean(true);
	}
}