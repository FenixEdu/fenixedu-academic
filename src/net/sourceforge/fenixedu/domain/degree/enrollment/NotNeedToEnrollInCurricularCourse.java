package net.sourceforge.fenixedu.domain.degree.enrollment;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/**
 * @author David Santos in Jun 17, 2004
 */

public class NotNeedToEnrollInCurricularCourse extends DomainObject implements
        INotNeedToEnrollInCurricularCourse {
    protected Integer curricularCourseKey;

    protected Integer studentCurricularPlanKey;

    protected ICurricularCourse curricularCourse;

    protected IStudentCurricularPlan studentCurricularPlan;

    public NotNeedToEnrollInCurricularCourse() {
        super();
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof INotNeedToEnrollInCurricularCourse) {
            INotNeedToEnrollInCurricularCourse o = (INotNeedToEnrollInCurricularCourse) obj;
            result = this.getCurricularCourse().equals(o.getCurricularCourse())
                    && this.getStudentCurricularPlan().equals(o.getStudentCurricularPlan());
        }
        return result;
    }

    /**
     * @return Returns the curricularCourse.
     */
    public ICurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    /**
     * @param curricularCourse
     *            The curricularCourse to set.
     */
    public void setCurricularCourse(ICurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    /**
     * @return Returns the curricularCourseKey.
     */
    public Integer getCurricularCourseKey() {
        return curricularCourseKey;
    }

    /**
     * @param curricularCourseKey
     *            The curricularCourseKey to set.
     */
    public void setCurricularCourseKey(Integer curricularCourseKey) {
        this.curricularCourseKey = curricularCourseKey;
    }

    /**
     * @return Returns the studentCurricularPlan.
     */
    public IStudentCurricularPlan getStudentCurricularPlan() {
        return studentCurricularPlan;
    }

    /**
     * @param studentCurricularPlan
     *            The studentCurricularPlan to set.
     */
    public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    /**
     * @return Returns the studentCurricularPlanKey.
     */
    public Integer getStudentCurricularPlanKey() {
        return studentCurricularPlanKey;
    }

    /**
     * @param studentCurricularPlanKey
     *            The studentCurricularPlanKey to set.
     */
    public void setStudentCurricularPlanKey(Integer studentCurricularPlanKey) {
        this.studentCurricularPlanKey = studentCurricularPlanKey;
    }
}