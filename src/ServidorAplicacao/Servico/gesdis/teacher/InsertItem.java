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
import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;


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

	throws ExcepcaoPersistencia {

		ISuportePersistente persistentSuport = null;
		IPersistentItem persistentItem = persistentSuport.getIPersistentItem();

		List itemsList = section.getItems();

		Iterator iterItems = itemsList.iterator();

		while (iterItems.hasNext()) {

			IItem item = (IItem) iterItems.next();
			int itemOrder = item.getItemOrder().intValue();

			if (itemOrder >= insertItemOrder) 
				item.setItemOrder(new Integer(itemOrder+1));

		   }


	}
	
	
	public void run (
	 	InfoSection infoSection,
	 	InfoItem infoItem) 
	 	throws FenixServiceException {
	 	
	 	try{
	 	
			IItem item = null;
			
			ISuportePersistente persistentSuport = null;
			IPersistentItem persistentItem = persistentSuport.getIPersistentItem();

	 		ISection section = Cloner.copyInfoSection2ISection(infoSection);
			
			item = Cloner.copyInfoItem2IItem(infoItem);
			
			persistentItem.lockWrite(item);
			
			organizeExistingItemsOrder(section, infoItem.getOrder().intValue());

	 		}

		catch (ExistingPersistentException e) {
			throw new ExistingServiceException(e);}

		catch (ExcepcaoPersistencia excepcaoPersistencia){
		
		throw new FenixServiceException(excepcaoPersistencia);
		}
	 }
}