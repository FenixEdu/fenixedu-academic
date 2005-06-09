package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IWebSite;
import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.IWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 24/09/2003
 * 
 */
public class ReadWebSiteByObjectCode implements IService {

    public InfoWebSite run(Integer webSiteCode) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentWebSite persistentWebSite = sp.getIPersistentWebSite();
        final IPersistentWebSiteSection persistentWebSiteSection = sp.getIPersistentWebSiteSection();
        final IPersistentWebSiteItem persistentWebSiteItem = sp.getIPersistentWebSiteItem();


        final List infoWebSiteSections = new ArrayList();

        final IWebSite webSite = (IWebSite) persistentWebSite.readByOID(WebSite.class, webSiteCode);

        if (webSite == null) {
            throw new NonExistingServiceException("message.nonExistingWebSite", null);
        }

        List webSiteSections = persistentWebSiteSection.readByWebSite(webSite);
        Iterator iterSections = webSiteSections.iterator();
        while (iterSections.hasNext()) {
            IWebSiteSection section = (IWebSiteSection) iterSections.next();

            InfoWebSiteSection infoWebSiteSection = Cloner
                    .copyIWebSiteSection2InfoWebSiteSection(section);

            List webSiteItems = persistentWebSiteItem.readAllWebSiteItemsByWebSiteSection(section);
            List infoWebSiteItems = (List) CollectionUtils.collect(webSiteItems, new Transformer() {
                public Object transform(Object arg0) {
                    IWebSiteItem webSiteItem = (IWebSiteItem) arg0;
                    InfoWebSiteItem infoWebSiteItem = Cloner
                            .copyIWebSiteItem2InfoWebSiteItem(webSiteItem);

                    return infoWebSiteItem;
                }
            });

            
            if (infoWebSiteSection.getSortingOrder().equals("descendent")) {
                ComparatorChain comparatorChain = new ComparatorChain();
                comparatorChain.addComparator(new BeanComparator("creationDate"), true);
                Collections.sort(infoWebSiteItems, comparatorChain);
            } else {
                Collections.sort(infoWebSiteItems, new BeanComparator("creationDate"));
            }

            infoWebSiteSection.setInfoItemsList(infoWebSiteItems);

            infoWebSiteSections.add(Cloner.copyIWebSiteSection2InfoWebSiteSection(section));
        }

        InfoWebSite infoWebSite = new InfoWebSite();
        infoWebSite.setName(webSite.getName());
        infoWebSite.setSections(infoWebSiteSections);

        return infoWebSite;
    }
}