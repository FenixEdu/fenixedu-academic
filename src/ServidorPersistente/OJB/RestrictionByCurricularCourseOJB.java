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
import ServidorPersistente.IPersistentRestrictionByCurricularCourse;

/**
 * @author jpvl
 */
public class RestrictionByCurricularCourseOJB extends RestrictionOJB implements
        IPersistentRestrictionByCurricularCourse {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.OJB.IPersistentRestriction#readByCurricularCourse(Dominio.ICurricularCourse)
     */
    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
        return readByCurricularCourseAndRestrictionClass(curricularCourse,
                RestrictionByCurricularCourse.class);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentRestriction#readByCurricularCourseAndRestrictionClass(Dominio.ICurricularCourse,
     *      java.lang.Class)
     */
    public List readByCurricularCourseAndRestrictionClass(ICurricularCourse curricularCourse, Class clazz)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();

        crit.addEqualTo("precedentCurricularCourse.idInternal", curricularCourse.getIdInternal());
        crit.addEqualTo("ojbConcreteClass", clazz.getName());

        return queryList(RestrictionByCurricularCourse.class, crit);
    }

}