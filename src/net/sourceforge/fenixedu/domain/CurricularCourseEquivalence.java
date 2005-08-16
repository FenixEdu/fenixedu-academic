package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author David Santos in Jun 29, 2004
 */

public class CurricularCourseEquivalence extends CurricularCourseEquivalence_Base {

    public CurricularCourseEquivalence() {
    }

    public CurricularCourseEquivalence(final IDegreeCurricularPlan degreeCurricularPlan,
            final ICurricularCourse curricularCourse, final ICurricularCourse oldCurricularCourse) {
        for (final ICurricularCourseEquivalence curricularCourseEquivalence : curricularCourse.getCurricularCourseEquivalences()) {
            if (oldCurricularCourse.equals(curricularCourseEquivalence.getOldCurricularCourse())) {
                throw new DomainException("error.exists.curricular.course.equivalency");
            }
        }

        setDegreeCurricularPlan(degreeCurricularPlan);
        setEquivalentCurricularCourse(curricularCourse);
        setOldCurricularCourse(oldCurricularCourse);
    }

}