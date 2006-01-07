package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.gesdis.CourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateCourseReports implements IService {

    public void run(Integer executionPeriodID) throws ExcepcaoPersistencia {

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Set<Integer> courseReportsExecutionCoursesIDs = new HashSet<Integer>();

        List<ExecutionCourse> executionCourses = ps.getIPersistentExecutionCourse()
                .readByExecutionPeriod(executionPeriodID);

        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getCourseReport() == null) {
                for (Evaluation evaluation : (List<Evaluation>) executionCourse
                        .getAssociatedEvaluations()) {
                    if (evaluation instanceof FinalEvaluation) {

                        if (courseReportsExecutionCoursesIDs.add(executionCourse.getIdInternal())) {
                            CourseReport courseReport = DomainFactory.makeCourseReport();
                            courseReport.setExecutionCourse(executionCourse);                                                        
                        }

                    }
                }
            }
        }

    }

}
