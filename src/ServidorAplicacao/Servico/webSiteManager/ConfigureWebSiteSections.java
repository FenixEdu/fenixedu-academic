package ServidorAplicacao.Servico.webSiteManager;

import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoWebSite;
import DataBeans.InfoWebSiteSection;
import Dominio.IWebSite;
import Dominio.IWebSiteSection;
import Dominio.WebSite;
import Dominio.WebSiteSection;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSite;
import ServidorPersistente.IPersistentWebSiteSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 22/Abr/2004
 * 
 */
public class ConfigureWebSiteSections implements IService {

	public ConfigureWebSiteSections() {

	}

	public boolean run(InfoWebSite infoWebSite) throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentWebSiteSection persistentWebSiteSection = persistentSuport.getIPersistentWebSiteSection();
			IPersistentWebSite persistentWebSite = persistentSuport.getIPersistentWebSite();
			
			IWebSiteSection webSiteSection = null;
			Iterator iterSections = infoWebSite.getSections().iterator();
			while(iterSections.hasNext()) {
			    InfoWebSiteSection infoWebSiteSection = (InfoWebSiteSection) iterSections.next();
			    
			    IWebSite webSite = new WebSite();
			    webSite.setIdInternal(infoWebSite.getIdInternal());
			    webSite = (IWebSite) persistentWebSite.readByOId(webSite, false);
			    
			    if(webSite == null) {
					throw new NonExistingServiceException("website");
			    }
			    
			    IWebSiteSection repeatedWebSiteSection = persistentWebSiteSection.readByWebSiteAndName(webSite, infoWebSiteSection.getName());
			    
			    if(repeatedWebSiteSection != null && 
			            !repeatedWebSiteSection.getIdInternal().equals(infoWebSiteSection.getIdInternal())) 
			    {
			        throw new ExistingServiceException(infoWebSiteSection.getName());
			    }
			    
			    webSiteSection = new WebSiteSection();
				webSiteSection.setIdInternal(infoWebSiteSection.getIdInternal());
				webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOId(webSiteSection, true);
				
				if(webSiteSection == null){
					throw new NonExistingServiceException("websiteSection");
				}
				webSiteSection.setName(infoWebSiteSection.getName());
				webSiteSection.setFtpName(infoWebSiteSection.getFtpName());
				webSiteSection.setWhatToSort(infoWebSiteSection.getWhatToSort());
				webSiteSection.setSortingOrder(infoWebSiteSection.getSortingOrder());
				webSiteSection.setSize(infoWebSiteSection.getSize());
				webSiteSection.setExcerptSize(infoWebSiteSection.getExcerptSize());
			}
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		return true;
	}
}