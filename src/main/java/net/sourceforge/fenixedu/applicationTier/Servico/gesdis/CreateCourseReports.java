package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateCourseReports {

    @Atomic
    public static void run(String executionPeriodID) {
        Set<String> courseReportsExecutionCoursesIDs = new HashSet<String>();

        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodID);
        Collection<ExecutionCourse> executionCourses = executionSemester.getAssociatedExecutionCourses();

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