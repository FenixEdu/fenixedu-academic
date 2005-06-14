package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.IWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 25/09/2003
 * 
 */
public class AddItem extends ManageWebSiteItem implements IService {

    public InfoWebSite run(Integer sectionCode, InfoWebSiteItem infoWebSiteItem, String user)
            throws FenixServiceException, ExcepcaoPersistencia {

        List infoWebSiteSections = new ArrayList();

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentWebSiteSection persistentWebSiteSection = persistentSuport
                .getIPersistentWebSiteSection();
        IPersistentWebSiteItem persistentWebSiteItem = persistentSuport.getIPersistentWebSiteItem();
        IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();

        IWebSiteSection webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOID(WebSiteSection.class,
                sectionCode);
        InfoWebSiteSection infoWebSiteSection = InfoWebSiteSection.newInfoFromDomain(webSiteSection);
        InfoWebSite infoWebSite = InfoWebSite.newInfoFromDomain(webSiteSection.getWebSite());
        infoWebSiteSection.setInfoWebSite(infoWebSite);


        checkData(infoWebSiteItem, webSiteSection);

        IWebSiteItem webSiteItem = new WebSiteItem();
        persistentWebSiteItem.simpleLockWrite(webSiteItem);

        fillWebSiteItemForDB(infoWebSiteItem, user, persistentPerson, persistentWebSiteSection,
                webSiteSection, webSiteItem);

        List webSiteSections = persistentWebSiteSection.readByWebSite(webSiteSection.getWebSite());
        Iterator iterSections = webSiteSections.iterator();
        while (iterSections.hasNext()) {
            IWebSiteSection section = (IWebSiteSection) iterSections.next();

            InfoWebSiteSection infoWebSiteSection2 = InfoWebSiteSection.newInfoFromDomain(section);

            infoWebSiteSections.add(infoWebSiteSection2);
        }

        infoWebSite.setSections(infoWebSiteSections);

        return infoWebSite;
    }
}