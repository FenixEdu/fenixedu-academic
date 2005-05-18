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

import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;

import org.apache.ojb.broker.query.Criteria;

public class SiteOJB extends PersistentObjectOJB implements IPersistentSite {

    public ISite readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyExecutionCourse", executionCourseID);
        return (ISite) queryObject(Site.class, crit);
    }
}