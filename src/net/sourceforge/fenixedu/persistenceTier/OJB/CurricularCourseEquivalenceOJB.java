package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseEquivalence;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author David Santos in Jun 29, 2004
 */

public class CurricularCourseEquivalenceOJB extends PersistentObjectOJB implements
        IPersistentCurricularCourseEquivalence {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentCurricularCourseEquivalence#readByOldCurricularCourse(Dominio.ICurricularCourse)
     */
    public List readByOldCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("oldCurricularCourse.idInternal", curricularCourse.getIdInternal());
        return queryList(CurricularCourseEquivalence.class, crit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentCurricularCourseEquivalence#readByEquivalentCurricularCourse(Dominio.ICurricularCourse)
     */
    public List readByEquivalentCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("equivalentCurricularCourse.idInternal", curricularCourse.getIdInternal());
        return queryList(CurricularCourseEquivalence.class, crit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentCurricularCourseEquivalence#readByEquivalence(Dominio.ICurricularCourse,
     *      Dominio.ICurricularCourse)
     */
    public ICurricularCourseEquivalence readByEquivalence(ICurricularCourse oldCurricularCourse,
            ICurricularCourse equivalentCurricularCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("equivalentCurricularCourse.idInternal", equivalentCurricularCourse
                .getIdInternal());
        crit.addEqualTo("oldCurricularCourse.idInternal", oldCurricularCourse.getIdInternal());
        crit.addEqualTo("degreeCurricularPlan.idInternal", equivalentCurricularCourse
                .getDegreeCurricularPlan().getIdInternal());
        return (ICurricularCourseEquivalence) queryObject(CurricularCourseEquivalence.class, crit);
    }
}