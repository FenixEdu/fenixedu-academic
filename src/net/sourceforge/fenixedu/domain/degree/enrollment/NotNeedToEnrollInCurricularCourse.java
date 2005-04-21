package net.sourceforge.fenixedu.domain.degree.enrollment;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/**
 * @author David Santos in Jun 17, 2004
 */

public class NotNeedToEnrollInCurricularCourse extends NotNeedToEnrollInCurricularCourse_Base {

    protected ICurricularCourse curricularCourse;

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
}