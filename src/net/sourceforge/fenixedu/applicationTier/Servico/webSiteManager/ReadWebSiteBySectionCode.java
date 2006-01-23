package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério 25/09/2003
 * 
 */
public class ReadWebSiteBySectionCode extends Service {

    public InfoWebSite run(Integer webSiteSectionCode) throws FenixServiceException,
            ExcepcaoPersistencia {

        List infoWebSiteSections = new ArrayList();
        List infoWebSiteItems = new ArrayList();

        IPersistentWebSiteSection persistentWebSiteSection = persistentSupport.getIPersistentWebSiteSection();
        IPersistentWebSiteItem persistentWebSiteItem = persistentSupport.getIPersistentWebSiteItem();

        WebSiteSection webSiteSection;
        webSiteSection = (WebSiteSection) persistentObject.readByOID(WebSiteSection.class,
                webSiteSectionCode);

        if (webSiteSection == null) {
            throw new NonExistingServiceException();
        }

        List webSiteSections = persistentWebSiteSection.readByWebSite(webSiteSection.getWebSite());
        Iterator iterSections = webSiteSections.iterator();
        while (iterSections.hasNext()) {
            WebSiteSection section = (WebSiteSection) iterSections.next();
            InfoWebSiteSection infoWebSiteSection = InfoWebSiteSection.newInfoFromDomain(section);

            List webSiteItems = persistentWebSiteItem.readAllWebSiteItemsByWebSiteSection(section);
            infoWebSiteItems = (List) CollectionUtils.collect(webSiteItems, new Transformer() {
                public Object transform(Object arg0) {
                    WebSiteItem webSiteItem = (WebSiteItem) arg0;
                    InfoWebSiteItem infoWebSiteItem = InfoWebSiteItem.newInfoFromDomain(webSiteItem);
                    return infoWebSiteItem;
                }
            });

            Collections.sort(infoWebSiteItems, new BeanComparator("creationDate"));
            if (infoWebSiteSection.getSortingOrder().equals("descendent")) {
                Collections.reverse(infoWebSiteItems);
            }

            infoWebSiteSection.setInfoItemsList(infoWebSiteItems);

            infoWebSiteSections.add(infoWebSiteSection);

        }

        InfoWebSite infoWebSite = new InfoWebSite();
        infoWebSite.setSections(infoWebSiteSections);
        infoWebSite.setName(webSiteSection.getWebSite().getName());

        return infoWebSite;
    }

}