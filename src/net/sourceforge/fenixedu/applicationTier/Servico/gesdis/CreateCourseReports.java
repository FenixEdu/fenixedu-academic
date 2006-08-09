package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateCourseReports extends Service {

    public void run(Integer executionPeriodID) throws ExcepcaoPersistencia {
        Set<Integer> courseReportsExecutionCoursesIDs = new HashSet<Integer>();

        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
        List<ExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getCourseReport() == null) {
                for (Evaluation evaluation : (List<Evaluation>) executionCourse
                        .getAssociatedEvaluations()) {
                    if (evaluation instanceof FinalEvaluation) {

                        if (courseReportsExecutionCoursesIDs.add(executionCourse.getIdInternal())) {
                            CourseReport courseReport = new CourseReport();
                            courseReport.setExecutionCourse(executionCourse);                                                        
                        }

                    }
                }
            }
        }

    }

}
