package net.sourceforge.fenixedu.domain;

/**
 * @author David Santos in Jun 29, 2004
 */

public class CurricularCourseEquivalence extends CurricularCourseEquivalence_Base {

    protected ICurricularCourse oldCurricularCourse;

    protected ICurricularCourse equivalentCurricularCourse;

    protected IDegreeCurricularPlan degreeCurricularPlan;

    public CurricularCourseEquivalence() {
    }

    public IDegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public ICurricularCourse getEquivalentCurricularCourse() {
        return equivalentCurricularCourse;
    }

    public void setEquivalentCurricularCourse(ICurricularCourse equivalentCurricularCourse) {
        this.equivalentCurricularCourse = equivalentCurricularCourse;
    }

    public ICurricularCourse getOldCurricularCourse() {
        return oldCurricularCourse;
    }

    public void setOldCurricularCourse(ICurricularCourse oldCurricularCourse) {
        this.oldCurricularCourse = oldCurricularCourse;
    }

}