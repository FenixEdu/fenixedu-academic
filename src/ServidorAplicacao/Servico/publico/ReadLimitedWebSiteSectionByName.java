package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoWebSiteItem;
import DataBeans.InfoWebSiteSection;
import DataBeans.util.Cloner;
import Dominio.IWebSiteItem;
import Dominio.IWebSiteSection;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSiteItem;
import ServidorPersistente.IPersistentWebSiteSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério 02/10/2003
 *  
 */
public class ReadLimitedWebSiteSectionByName implements IServico {
    private static ReadLimitedWebSiteSectionByName _servico = new ReadLimitedWebSiteSectionByName();

    /**
     * The actor of this class.
     */
    private ReadLimitedWebSiteSectionByName() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadLimitedWebSiteSectionByName";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadLimitedWebSiteSectionByName
     */
    public static ReadLimitedWebSiteSectionByName getService() {
        return _servico;
    }

    public Object run(String sectionName) throws ExcepcaoInexistente, FenixServiceException {

        IWebSiteSection webSiteSection = null;
        InfoWebSiteSection infoWebSiteSection = new InfoWebSiteSection();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentWebSiteSection persistentWebSiteSection = sp.getIPersistentWebSiteSection();
            IPersistentWebSiteItem persistentWebSiteItem = sp.getIPersistentWebSiteItem();

            webSiteSection = persistentWebSiteSection.readByName(sectionName);

            if (webSiteSection == null) {
                throw new NonExistingServiceException();
            }

            List webSiteItems = persistentWebSiteItem
                    .readPublishedWebSiteItemsByWebSiteSection(webSiteSection);

            if (webSiteItems == null || webSiteItems.size() == 0) {
                throw new NonExistingServiceException();
            }

            // get items with valid dates of publishment
            CollectionUtils.filter(webSiteItems, new Predicate() {
                public boolean evaluate(Object arg0) {
                    IWebSiteItem webSiteItem = (IWebSiteItem) arg0;
                    if (!webSiteItem.getOnlineBeginDay().after(Calendar.getInstance().getTime())
                            && !webSiteItem.getOnlineEndDay().before(Calendar.getInstance().getTime())) {
                        return true;
                    }

                    return false;
                }
            });
            if (webSiteItems.size() == 0) {
                throw new NonExistingServiceException();
            }

            List infoWebSiteItems = (List) CollectionUtils.collect(webSiteItems, new Transformer() {
                public Object transform(Object arg0) {
                    IWebSiteItem webSiteItem = (IWebSiteItem) arg0;
                    InfoWebSiteItem infoWebSiteItem = Cloner
                            .copyIWebSiteItem2InfoWebSiteItem(webSiteItem);

                    return infoWebSiteItem;
                }
            });

            Collections.sort(infoWebSiteItems, new BeanComparator("creationDate"));
            if (webSiteSection.getSortingOrder().equals("descendent")) {
                Collections.reverse(infoWebSiteItems);
            }

            // limits number of items to mandatory section size in website
            if (infoWebSiteItems.size() > webSiteSection.getSize().intValue()) {
                List limitedList = new ArrayList();
                ListIterator iterItems = infoWebSiteItems.listIterator();
                while (iterItems.previousIndex() <= webSiteSection.getSize().intValue()) {
                    InfoWebSiteItem infoWebSiteItem = (InfoWebSiteItem) iterItems.next();
                    limitedList.add(infoWebSiteItem);
                }
                infoWebSiteSection.setInfoItemsList(limitedList);
            } else {
                infoWebSiteSection.setInfoItemsList(infoWebSiteItems);
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadSection");
        }

        infoWebSiteSection.setIdInternal(webSiteSection.getIdInternal());
        infoWebSiteSection.setExcerptSize(webSiteSection.getExcerptSize());
        infoWebSiteSection.setInfoWebSite(Cloner.copyIWebSite2InfoWebSite(webSiteSection.getWebSite()));
        infoWebSiteSection.setName(webSiteSection.getName());
        infoWebSiteSection.setSize(webSiteSection.getSize());
        infoWebSiteSection.setSortingOrder(webSiteSection.getSortingOrder());

        return infoWebSiteSection;
    }
}