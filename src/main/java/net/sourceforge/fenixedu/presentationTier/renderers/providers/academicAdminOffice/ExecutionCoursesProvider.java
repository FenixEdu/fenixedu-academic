package net.sourceforge.fenixedu.presentationTier.renderers.providers.academicAdminOffice;

import java.util.HashSet;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.ExecutionCourseBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class ExecutionCoursesProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object arg0, Object arg1) {
        ExecutionCourseBean bean = (ExecutionCourseBean) arg0;
        ExecutionSemester executionSemester = bean.getExecutionSemester();
        HashSet<ExecutionCourse> result = new HashSet<ExecutionCourse>();
        if (executionSemester != null) {
            for (ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCourses()) {
                for (ExecutionDegree degree : executionCourse.getExecutionDegrees()) {
                    if (AcademicPredicates.MANAGE_EXECUTION_COURSES.evaluate(degree.getDegree())) {
                        result.add(executionCourse);
                        break;
                    }
                }
            }
        }
        return result;
    }

}
