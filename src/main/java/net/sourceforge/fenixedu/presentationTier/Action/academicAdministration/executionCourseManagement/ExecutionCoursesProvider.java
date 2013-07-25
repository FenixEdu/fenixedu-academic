package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

@Deprecated
public class ExecutionCoursesProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {

        ExecutionCourseManagementBean bean = (ExecutionCourseManagementBean) source;

        ExecutionSemester semester = bean.getSemester();
        CurricularYear curricularYear = bean.getCurricularYear();
        DegreeCurricularPlan plan = bean.getDegreeCurricularPlan();

        return plan.getExecutionCoursesByExecutionPeriodAndSemesterAndYear(semester, curricularYear.getYear(),
                semester.getSemester());
    }

}
