package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IWebSite;
import Dominio.IWebSiteSection;
import Dominio.WebSiteSection;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSiteSection;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public class WebSiteSectionOJB extends PersistentObjectOJB implements IPersistentWebSiteSection {

    /** Creates a new instance of SeccaoOJB */
    public WebSiteSectionOJB() {
    }

    public IWebSiteSection readByWebSiteAndName(IWebSite webSite, String name)
            throws ExcepcaoPersistencia {

        IWebSiteSection resultWebSiteSection = null;
        Criteria criteria = new Criteria();

        criteria.addEqualTo("name", name);
        criteria.addEqualTo("webSite.name", webSite.getName());

        resultWebSiteSection = (IWebSiteSection) queryObject(WebSiteSection.class, criteria);

        return resultWebSiteSection;
    }

    public List readByWebSite(IWebSite webSite) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("webSite.name", webSite.getName());
        List result = queryList(WebSiteSection.class, criteria);

        return result;
    }

    public IWebSiteSection readByName(String name) throws ExcepcaoPersistencia {

        IWebSiteSection resultWebSiteSection = null;
        Criteria criteria = new Criteria();

        criteria.addEqualTo("name", name);

        resultWebSiteSection = (IWebSiteSection) queryObject(WebSiteSection.class, criteria);

        return resultWebSiteSection;
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        List result = queryList(WebSiteSection.class, criteria);
        return result;
    }

    public void delete(IWebSiteSection section) throws ExcepcaoPersistencia {

        super.delete(section);

    }

}