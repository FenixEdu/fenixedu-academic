package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSite;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;

/**
 * @author Fernanda Quitério 22/Abr/2004
 * 
 */
public class ConfigureWebSiteSections extends Service {

	public boolean run(InfoWebSite infoWebSite) throws FenixServiceException, ExcepcaoPersistencia {
		IPersistentWebSiteSection persistentWebSiteSection = persistentSupport
				.getIPersistentWebSiteSection();
		WebSiteSection webSiteSection = null;
		Iterator iterSections = infoWebSite.getSections().iterator();
		while (iterSections.hasNext()) {
			InfoWebSiteSection infoWebSiteSection = (InfoWebSiteSection) iterSections.next();

			WebSite webSite = (WebSite) persistentObject.readByOID(WebSite.class, infoWebSite
					.getIdInternal());

			if (webSite == null) {
				throw new NonExistingServiceException("website");
			}

			WebSiteSection repeatedWebSiteSection = persistentWebSiteSection.readByWebSiteAndName(
					webSite, infoWebSiteSection.getName());

			if (repeatedWebSiteSection != null
					&& !repeatedWebSiteSection.getIdInternal()
							.equals(infoWebSiteSection.getIdInternal())) {
				throw new ExistingServiceException(infoWebSiteSection.getName());
			}

			webSiteSection = (WebSiteSection) persistentObject.readByOID(WebSiteSection.class,
					infoWebSiteSection.getIdInternal());

			if (webSiteSection == null) {
				throw new NonExistingServiceException("websiteSection");
			}
			webSiteSection.setName(infoWebSiteSection.getName());
			webSiteSection.setFtpName(infoWebSiteSection.getFtpName());
			webSiteSection.setWhatToSort(infoWebSiteSection.getWhatToSort());
			webSiteSection.setSortingOrder(infoWebSiteSection.getSortingOrder());
			webSiteSection.setSize(infoWebSiteSection.getSize());
			webSiteSection.setExcerptSize(infoWebSiteSection.getExcerptSize());
		}
		return true;
	}
}