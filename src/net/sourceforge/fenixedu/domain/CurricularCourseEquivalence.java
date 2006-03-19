package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author David Santos in Jun 29, 2004
 */

public class CurricularCourseEquivalence extends CurricularCourseEquivalence_Base {

    public CurricularCourseEquivalence() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public CurricularCourseEquivalence(final DegreeCurricularPlan degreeCurricularPlan,
            final CurricularCourse curricularCourse, final CurricularCourse oldCurricularCourse) {
    	this();
        for (final CurricularCourseEquivalence curricularCourseEquivalence : curricularCourse.getCurricularCourseEquivalences()) {
            if (oldCurricularCourse.equals(curricularCourseEquivalence.getOldCurricularCourse())) {
                throw new DomainException("error.exists.curricular.course.equivalency");
            }
        }

        setDegreeCurricularPlan(degreeCurricularPlan);
        setEquivalentCurricularCourse(curricularCourse);
        setOldCurricularCourse(oldCurricularCourse);
    }

    public void delete() {
        setDegreeCurricularPlan(null);
        setEquivalentCurricularCourse(null);
        setOldCurricularCourse(null);

        super.deleteDomainObject();
    }

}