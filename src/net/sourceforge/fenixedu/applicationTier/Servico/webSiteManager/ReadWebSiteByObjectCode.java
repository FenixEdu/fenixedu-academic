package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IWebSite;
import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.WebSite;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério 24/09/2003
 *  
 */
public class ReadWebSiteByObjectCode implements IServico {
    private static ReadWebSiteByObjectCode _servico = new ReadWebSiteByObjectCode();

    /**
     * The actor of this class.
     */
    private ReadWebSiteByObjectCode() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadWebSiteByObjectCode";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadWebSiteByObjectCode
     */
    public static ReadWebSiteByObjectCode getService() {
        return _servico;
    }

    public InfoWebSite run(Integer webSiteCode) throws ExcepcaoInexistente, FenixServiceException {

        IWebSite webSite = null;
        List infoWebSiteSections = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentWebSite persistentWebSite = sp.getIPersistentWebSite();
            IPersistentWebSiteSection persistentWebSiteSection = sp.getIPersistentWebSiteSection();
            IPersistentWebSiteItem persistentWebSiteItem = sp.getIPersistentWebSiteItem();

            webSite = (IWebSite) persistentWebSite.readByOID(WebSite.class, webSiteCode);

            if (webSite == null) {
                throw new NonExistingServiceException("message.nonExistingWebSite", null);
            }

            List webSiteSections = persistentWebSiteSection.readByWebSite(webSite);
            Iterator iterSections = webSiteSections.iterator();
            while (iterSections.hasNext()) {
                WebSiteSection section = (WebSiteSection) iterSections.next();

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

                Collections.sort(infoWebSiteItems, new BeanComparator("creationDate"));
                if (infoWebSiteSection.getSortingOrder().equals("descendent")) {
                    Collections.reverse(infoWebSiteItems);
                }

                infoWebSiteSection.setInfoItemsList(infoWebSiteItems);

                infoWebSiteSections.add(Cloner.copyIWebSiteSection2InfoWebSiteSection(section));
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        InfoWebSite infoWebSite = new InfoWebSite();
        infoWebSite.setName(webSite.getName());
        infoWebSite.setSections(infoWebSiteSections);

        return infoWebSite;
    }
}