/*
 * Created on 7/Abr/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRestriction;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 * @author David Santos in Aug 2, 2004
 */

public class RestrictionOJB extends PersistentObjectOJB implements IPersistentRestriction {

    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
        return readByCurricularCourseAndRestrictionClass(curricularCourse,
                RestrictionByCurricularCourse.class);
    }

    public List readByCurricularCourseAndRestrictionClass(ICurricularCourse curricularCourse, Class clazz)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();

        crit.addEqualTo("precedentCurricularCourse.idInternal", curricularCourse.getIdInternal());
        crit.addEqualTo("ojbConcreteClass", clazz.getName());

        return queryList(RestrictionByCurricularCourse.class, crit);
    }

}