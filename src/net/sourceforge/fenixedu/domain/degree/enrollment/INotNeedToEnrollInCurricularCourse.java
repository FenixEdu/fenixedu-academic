package net.sourceforge.fenixedu.domain.degree.enrollment;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/**
 * @author David Santos in Jun 17, 2004
 */

public interface INotNeedToEnrollInCurricularCourse {
    public ICurricularCourse getCurricularCourse();

    public void setCurricularCourse(ICurricularCourse curricularCourse);

    public IStudentCurricularPlan getStudentCurricularPlan();

    public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
}