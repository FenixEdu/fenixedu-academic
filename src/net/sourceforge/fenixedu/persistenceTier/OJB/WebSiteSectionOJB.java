package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.WebSite;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public class WebSiteSectionOJB extends PersistentObjectOJB implements IPersistentWebSiteSection {

    /** Creates a new instance of SeccaoOJB */
    public WebSiteSectionOJB() {
    }

    public WebSiteSection readByWebSiteAndName(WebSite webSite, String name)
            throws ExcepcaoPersistencia {

        WebSiteSection resultWebSiteSection = null;
        Criteria criteria = new Criteria();

        criteria.addEqualTo("name", name);
        criteria.addEqualTo("webSite.name", webSite.getName());

        resultWebSiteSection = (WebSiteSection) queryObject(WebSiteSection.class, criteria);

        return resultWebSiteSection;
    }

    public List readByWebSite(WebSite webSite) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("webSite.name", webSite.getName());
        List result = queryList(WebSiteSection.class, criteria);

        return result;
    }

    public WebSiteSection readByName(String name) throws ExcepcaoPersistencia {

        WebSiteSection resultWebSiteSection = null;
        Criteria criteria = new Criteria();

        criteria.addEqualTo("name", name);

        resultWebSiteSection = (WebSiteSection) queryObject(WebSiteSection.class, criteria);

        return resultWebSiteSection;
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        List result = queryList(WebSiteSection.class, criteria);
        return result;
    }

    public void delete(WebSiteSection section) throws ExcepcaoPersistencia {

        super.delete(section);

    }

}