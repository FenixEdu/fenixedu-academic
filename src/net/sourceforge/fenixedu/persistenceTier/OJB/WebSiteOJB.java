package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IWebSite;
import net.sourceforge.fenixedu.domain.WebSite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSite;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public class WebSiteOJB extends PersistentObjectOJB implements IPersistentWebSite {

    /** Creates a new instance of SitioOJB */
    public WebSiteOJB() {
    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        return queryList(WebSite.class, crit);
    }

    public void delete(IWebSite site) throws ExcepcaoPersistencia {
        super.delete(site);
    }

}