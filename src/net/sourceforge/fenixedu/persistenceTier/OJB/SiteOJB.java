/*
 * SitioOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * 
 * @author ars
 */

//import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;

import org.apache.ojb.broker.query.Criteria;

public class SiteOJB extends PersistentObjectOJB implements IPersistentSite {

    /** Creates a new instance of SitioOJB */
    public SiteOJB() {
    }

    public ISite readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        return (ISite) queryObject(Site.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        return queryList(Site.class, crit);
    }

    public void delete(ISite site) throws ExcepcaoPersistencia {
        super.delete(site);
    }

}