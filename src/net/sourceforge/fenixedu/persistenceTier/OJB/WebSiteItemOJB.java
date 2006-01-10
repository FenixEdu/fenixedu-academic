package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Fernanda Quitério 23/09/2003
 */
public class WebSiteItemOJB extends PersistentObjectOJB implements IPersistentWebSiteItem {

    public WebSiteItem readByWebSiteSectionAndName(WebSiteSection webSiteSection, String title)
            throws ExcepcaoPersistencia {
        WebSiteItem item = null;
        if (webSiteSection != null && title != null) {
            Criteria criteria = new Criteria();

            criteria.addEqualTo("webSiteSection.name", webSiteSection.getName());
            criteria.addEqualTo("title", title);

            item = (WebSiteItem) queryObject(WebSiteItem.class, criteria);
        }
        return item;
    }

    public List readAllWebSiteItemsByWebSiteSection(WebSiteSection webSiteSection)
            throws ExcepcaoPersistencia {
        List items = null;
        if (webSiteSection != null) {
            Criteria criteria = new Criteria();

            criteria.addEqualTo("webSiteSection.name", webSiteSection.getName());
            criteria.addEqualTo("webSiteSection.webSite.name", webSiteSection.getWebSite().getName());

            items = queryList(WebSiteItem.class, criteria, true);
        }
        return items;
    }

    public List readPublishedWebSiteItemsByWebSiteSection(WebSiteSection webSiteSection)
            throws ExcepcaoPersistencia {
        List items = null;
        if (webSiteSection != null) {
            Criteria criteria = new Criteria();

            criteria.addEqualTo("webSiteSection.name", webSiteSection.getName());
            criteria.addEqualTo("webSiteSection.webSite.name", webSiteSection.getWebSite().getName());
            criteria.addEqualTo("published", Boolean.TRUE);

            items = queryList(WebSiteItem.class, criteria, true);
        }
        return items;
    }

    public void delete(WebSiteItem item) throws ExcepcaoPersistencia {
        super.delete(item);
    }

}