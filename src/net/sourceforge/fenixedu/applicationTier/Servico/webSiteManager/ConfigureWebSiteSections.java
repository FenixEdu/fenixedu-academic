package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.domain.IWebSite;
import net.sourceforge.fenixedu.domain.IWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSite;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 22/Abr/2004
 *  
 */
public class ConfigureWebSiteSections implements IService {

    public ConfigureWebSiteSections() {

    }

    public boolean run(InfoWebSite infoWebSite) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentWebSiteSection persistentWebSiteSection = persistentSuport
                    .getIPersistentWebSiteSection();
            IPersistentWebSite persistentWebSite = persistentSuport.getIPersistentWebSite();

            IWebSiteSection webSiteSection = null;
            Iterator iterSections = infoWebSite.getSections().iterator();
            while (iterSections.hasNext()) {
                InfoWebSiteSection infoWebSiteSection = (InfoWebSiteSection) iterSections.next();

                IWebSite webSite = (IWebSite) persistentWebSite.readByOID(WebSite.class, infoWebSite
                        .getIdInternal());

                if (webSite == null) {
                    throw new NonExistingServiceException("website");
                }

                IWebSiteSection repeatedWebSiteSection = persistentWebSiteSection.readByWebSiteAndName(
                        webSite, infoWebSiteSection.getName());

                if (repeatedWebSiteSection != null
                        && !repeatedWebSiteSection.getIdInternal().equals(
                                infoWebSiteSection.getIdInternal())) {
                    throw new ExistingServiceException(infoWebSiteSection.getName());
                }

                webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOID(
                        WebSiteSection.class, infoWebSiteSection.getIdInternal(), true);

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
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        return true;
    }
}