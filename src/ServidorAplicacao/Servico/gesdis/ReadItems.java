/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoSection;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2 & lmac1
 */

public class ReadItems implements IServico {
    
	private static ReadItems service = new ReadItems();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadItems getService() {
		return service;
	}
    
	/**
	 * The ctor of this class.
	 **/
	private ReadItems() {
	}
    
	/**
	 * Returns the name of this service.
	 **/
	public final String getNome() {
		return "ReadItems";
	}
    
	/**
	 * Executes the service.
	 *
	 **/
	public List run(InfoSection infoSection) throws FenixServiceException {
		List itemsList = null;

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentItem persistentItem = sp.getIPersistentItem();

			ISection section = Cloner.copyInfoSection2ISection(infoSection);

			itemsList = persistentItem.readAllItemsBySection(section);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		List infoItemsList = new ArrayList(itemsList.size());
		Iterator iter = itemsList.iterator();
		
		while(iter.hasNext())
			infoItemsList.add(Cloner.copyIItem2InfoItem((IItem) iter.next()));
		

		//			if (itemsList == null || itemsList.isEmpty())
		//				throw new InvalidArgumentsServiceException();
		Collections.sort(infoItemsList);
		return infoItemsList;

	}

}


