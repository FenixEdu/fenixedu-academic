package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.IWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério 25/09/2003
 *  
 */
public class ReadWebSiteBySectionCode implements IServico {
    private static ReadWebSiteBySectionCode _servico = new ReadWebSiteBySectionCode();

    /**
     * The actor of this class.
     */
    private ReadWebSiteBySectionCode() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadWebSiteBySectionCode";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadWebSiteBySectionCode
     */
    public static ReadWebSiteBySectionCode getService() {
        return _servico;
    }

    public InfoWebSite run(Integer webSiteSectionCode) throws ExcepcaoInexistente, FenixServiceException {

        List infoWebSiteSections = new ArrayList();
        List infoWebSiteItems = new ArrayList();
        InfoWebSiteSection infoWebSiteSection2 = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentWebSiteSection persistentWebSiteSection = sp.getIPersistentWebSiteSection();
            IPersistentWebSiteItem persistentWebSiteItem = sp.getIPersistentWebSiteItem();

            IWebSiteSection webSiteSection;
            webSiteSection = (IWebSiteSection) persistentWebSiteSection.readByOID(WebSiteSection.class,
                    webSiteSectionCode);

            if (webSiteSection == null) {
                throw new NonExistingServiceException();
            }
            infoWebSiteSection2 = Cloner.copyIWebSiteSection2InfoWebSiteSection(webSiteSection);

            List webSiteSections = persistentWebSiteSection.readByWebSite(webSiteSection.getWebSite());
            Iterator iterSections = webSiteSections.iterator();
            while (iterSections.hasNext()) {
                WebSiteSection section = (WebSiteSection) iterSections.next();
                InfoWebSiteSection infoWebSiteSection = Cloner
                        .copyIWebSiteSection2InfoWebSiteSection(section);

                List webSiteItems = persistentWebSiteItem.readAllWebSiteItemsByWebSiteSection(section);
                infoWebSiteItems = (List) CollectionUtils.collect(webSiteItems, new Transformer() {
                    public Object transform(Object arg0) {
                        IWebSiteItem webSiteItem = (IWebSiteItem) arg0;
                        InfoWebSiteItem infoWebSiteItem = Cloner
                                .copyIWebSiteItem2InfoWebSiteItem(webSiteItem);

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

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        InfoWebSite infoWebSite = new InfoWebSite();
        infoWebSite.setSections(infoWebSiteSections);
        infoWebSite.setName(infoWebSiteSection2.getInfoWebSite().getName());

        return infoWebSite;
    }
}