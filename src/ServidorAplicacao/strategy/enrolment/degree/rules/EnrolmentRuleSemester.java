package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourse;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentRuleSemester implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {

		SuportePersistenteOJB persistentSupport = null;
		IPersistentCurricularCourse persistentCurricularCourse = null;

		List curricularCoursesFromActualExecutionPeriod = new ArrayList();
		List curricularCoursesBySemester = null;

		persistentSupport = SuportePersistenteOJB.getInstance();
		persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();

		curricularCoursesBySemester = persistentCurricularCourse.readAllCurricularCoursesBySemester(enrolmentContext.getSemester());

		ICurricularCourse curricularCourse = null;
		Iterator iterator = enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled().iterator();
		while (iterator.hasNext()) {
			curricularCourse = (ICurricularCourse) iterator.next();
			if (curricularCoursesBySemester.contains(curricularCourse)) {
				curricularCoursesFromActualExecutionPeriod.add(curricularCourse);
			}
		}

		enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(curricularCoursesFromActualExecutionPeriod);
		return enrolmentContext;
	}
}