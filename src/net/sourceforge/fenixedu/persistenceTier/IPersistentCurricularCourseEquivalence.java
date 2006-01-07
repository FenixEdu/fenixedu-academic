package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;

/**
 * @author David Santos in Jun 29, 2004
 */

public interface IPersistentCurricularCourseEquivalence extends IPersistentObject {

    public CurricularCourseEquivalence readByEquivalence(Integer oldCurricularCourseId,
            Integer equivalentCurricularCourseId, Integer degreeCurricularPlanId) throws ExcepcaoPersistencia;

}