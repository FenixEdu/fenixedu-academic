/*
 * Created on 7/Abr/2003 by jpvl
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICurricularCourse;
import Dominio.precedences.RestrictionByCurricularCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentRestriction;

/**
 * @author jpvl
 * @author David Santos in Aug 2, 2004
 */

public class RestrictionOJB extends ObjectFenixOJB implements IPersistentRestriction {

    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
        return readByCurricularCourseAndRestrictionClass(curricularCourse, RestrictionByCurricularCourse.class);
    }

    public List readByCurricularCourseAndRestrictionClass(ICurricularCourse curricularCourse, Class clazz)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();

        crit.addEqualTo("precedentCurricularCourse.idInternal", curricularCourse.getIdInternal());
        crit.addEqualTo("ojbConcreteClass", clazz.getName());

        return queryList(RestrictionByCurricularCourse.class, crit);
    }

}