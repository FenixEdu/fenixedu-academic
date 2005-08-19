package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IFinalEvaluation;
import net.sourceforge.fenixedu.domain.gesdis.ICourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.ojb.broker.core.proxy.ProxyHelper;

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

        List<IExecutionCourse> executionCourses = ps.getIPersistentExecutionCourse()
                .readByExecutionPeriod(executionPeriodID);

        for (IExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.getCourseReport() == null) {
                for (IEvaluation evaluation : (List<IEvaluation>) executionCourse
                        .getAssociatedEvaluations()) {

                    if (evaluation instanceof Proxy) {
                        evaluation = (IEvaluation) ProxyHelper.getRealObject(evaluation);
                    }

                    if (evaluation instanceof IFinalEvaluation) {

                        if (courseReportsExecutionCoursesIDs.add(executionCourse.getIdInternal())) {
                            ICourseReport courseReport = DomainFactory.makeCourseReport();
                            courseReport.setExecutionCourse(executionCourse);                                                        
                        }

                    }
                }
            }
        }

    }

}
