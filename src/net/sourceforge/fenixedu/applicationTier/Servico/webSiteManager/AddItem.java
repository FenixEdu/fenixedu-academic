package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
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

    public AddItem() {

    }

    //infoItem with an infoSection

    public InfoWebSite run(Integer sectionCode, InfoWebSiteItem infoWebSiteItem, String user)
            throws FenixServiceException {

        List infoWebSiteSections = new ArrayList();
        InfoWebSiteSection infoWebSiteSection = null;

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentWebSiteSection persistentWebSiteSection = persistentSuport
                    .getIPersistentWebSiteSection();
            IPersistentWebSiteItem persistentWebSiteItem = persistentSuport.getIPersistentWebSiteItem();
            IPessoaPersistente persistentPerson = persistentSuport.getIPessoaPersistente();

            IWebSiteSection webSiteSection;
            webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOID(WebSiteSection.class,
                    sectionCode);
            infoWebSiteSection = Cloner.copyIWebSiteSection2InfoWebSiteSection(webSiteSection);

            checkData(infoWebSiteItem, webSiteSection);

            IWebSiteItem webSiteItem = new WebSiteItem();
            persistentWebSiteItem.simpleLockWrite(webSiteItem);

            fillWebSiteItemForDB(infoWebSiteItem, user, persistentPerson, persistentWebSiteSection,
                    webSiteSection, webSiteItem);

            List webSiteSections = persistentWebSiteSection.readByWebSite(webSiteSection.getWebSite());
            Iterator iterSections = webSiteSections.iterator();
            while (iterSections.hasNext()) {
                WebSiteSection section = (WebSiteSection) iterSections.next();

                InfoWebSiteSection infoWebSiteSection2 = Cloner
                        .copyIWebSiteSection2InfoWebSiteSection(section);

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