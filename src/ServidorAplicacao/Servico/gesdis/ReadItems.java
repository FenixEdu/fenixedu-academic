/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.List;

import DataBeans.gesdis.InfoSection;
import DataBeans.util.Cloner;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac2
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
		List itemsList=null;
		ISection fatherSection = null;
		try {
        
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentSection persistentSection = sp.getIPersistentSection();
		IPersistentItem persistentItem = sp.getIPersistentItem();
		
		ISite site = Cloner.copyInfoSite2ISite(infoSection.getInfoSite());
		
		InfoSection superiorInfoSection = infoSection.getSuperiorInfoSection();
		if (superiorInfoSection!=null)
			fatherSection = Cloner.copyInfoSection2ISection(infoSection.getSuperiorInfoSection());
		
		ISection section = persistentSection.readBySiteAndSectionAndName(site,fatherSection,infoSection.getName());
		
		itemsList =persistentItem.readAllItemsBySection(section); 
		
		if (itemsList == null || itemsList.isEmpty()) throw new InvalidArgumentsServiceException();

        return itemsList;
        
       } catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
    
}


