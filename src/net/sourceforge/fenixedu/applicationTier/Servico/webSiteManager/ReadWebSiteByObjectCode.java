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
import net.sourceforge.fenixedu.domain.WebSite;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author Fernanda Quitério 24/09/2003
 * 
 */
public class ReadWebSiteByObjectCode extends Service {

    public InfoWebSite run(Integer webSiteCode) throws FenixServiceException, ExcepcaoPersistencia {
        final List infoWebSiteSections = new ArrayList();

        final WebSite webSite = rootDomainObject.readWebSiteByOID(webSiteCode);
        if (webSite == null) {
            throw new NonExistingServiceException("message.nonExistingWebSite", null);
        }

        List<WebSiteSection> webSiteSections = webSite.getAssociatedWebSiteSections(); 
        Iterator iterSections = webSiteSections.iterator();
        while (iterSections.hasNext()) {
            WebSiteSection section = (WebSiteSection) iterSections.next();

            InfoWebSiteSection infoWebSiteSection = InfoWebSiteSection.newInfoFromDomain(section);

            List<WebSiteItem> webSiteItems = section.getIncludedWebSiteItems(); 
            List<InfoWebSiteItem> infoWebSiteItems = (List) CollectionUtils.collect(webSiteItems, new Transformer() {
                public Object transform(Object arg0) {
                    WebSiteItem webSiteItem = (WebSiteItem) arg0;
                    InfoWebSiteItem infoWebSiteItem = InfoWebSiteItem.newInfoFromDomain(webSiteItem);

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

            infoWebSiteSections.add(InfoWebSiteSection.newInfoFromDomain(section));
        }

        InfoWebSite infoWebSite = new InfoWebSite();
        infoWebSite.setName(webSite.getName());
        infoWebSite.setSections(infoWebSiteSections);

        return infoWebSite;
    }
}