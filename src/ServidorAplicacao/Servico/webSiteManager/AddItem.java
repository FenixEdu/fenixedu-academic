package ServidorAplicacao.Servico.webSiteManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoWebSite;
import DataBeans.InfoWebSiteItem;
import DataBeans.InfoWebSiteSection;
import DataBeans.util.Cloner;
import Dominio.IWebSiteItem;
import Dominio.IWebSiteSection;
import Dominio.WebSiteItem;
import Dominio.WebSiteSection;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSiteItem;
import ServidorPersistente.IPersistentWebSiteSection;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 25/09/2003
 * 
 */
public class AddItem extends ManageWebSiteItem implements IService{

	public AddItem() {

	}

	//infoItem with an infoSection

	public InfoWebSite run(Integer sectionCode, InfoWebSiteItem infoWebSiteItem, String user) throws FenixServiceException {

		List infoWebSiteSections = new ArrayList();
		InfoWebSiteSection infoWebSiteSection = null;

		try {
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentWebSiteSection persistentWebSiteSection = persistentSuport.getIPersistentWebSiteSection();
			IPersistentWebSiteItem persistentWebSiteItem = persistentSuport.getIPersistentWebSiteItem();
			IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();
			
			IWebSiteSection webSiteSection = new WebSiteSection();
			webSiteSection.setIdInternal(sectionCode);
			webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOId(webSiteSection, false);
			infoWebSiteSection = Cloner.copyIWebSiteSection2InfoWebSiteSection(webSiteSection);

			checkData(infoWebSiteItem, webSiteSection);
			
			IWebSiteItem webSiteItem = new WebSiteItem();
			persistentWebSiteItem.simpleLockWrite(webSiteItem);

			fillWebSiteItemForDB(infoWebSiteItem, user, persistentPerson, persistentWebSiteSection, webSiteSection, webSiteItem);

			List webSiteSections = persistentWebSiteSection.readByWebSite(webSiteSection.getWebSite());
			Iterator iterSections = webSiteSections.iterator();
			while (iterSections.hasNext()) {
				WebSiteSection section = (WebSiteSection) iterSections.next();

				InfoWebSiteSection infoWebSiteSection2 = Cloner.copyIWebSiteSection2InfoWebSiteSection(section);

				infoWebSiteSections.add(infoWebSiteSection2);
			}
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		InfoWebSite infoWebSite = infoWebSiteSection.getInfoWebSite();
		infoWebSite.setSections(infoWebSiteSections);

		return infoWebSite;
	}
}