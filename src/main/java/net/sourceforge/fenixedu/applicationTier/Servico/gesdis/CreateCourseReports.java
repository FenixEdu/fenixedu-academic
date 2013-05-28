package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateCourseReports {

    @Service
    public static void run(Integer executionPeriodID) {
        Set<Integer> courseReportsExecutionCoursesIDs = new HashSet<Integer>();

        final ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodID);
        List<ExecutionCourse> executionCourses = executionSemester.getAssociatedExecutionCourses();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getCourseReport() == null) {
                for (Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
                    if (evaluation instanceof FinalEvaluation) {

                        if (courseReportsExecutionCoursesIDs.add(executionCourse.getExternalId())) {
                            CourseReport courseReport = new CourseReport();
                            courseReport.setExecutionCourse(executionCourse);
                        }

                    }
                }
            }
        }

    }

}