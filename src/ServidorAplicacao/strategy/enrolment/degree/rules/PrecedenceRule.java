/*
 * Created on 8/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IPrecedence;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * This class filter final curricular courses by precedences rules that are configurated for degreeCurricularPlan...
 * @author jpvl
 */
public class PrecedenceRule implements IEnrolmentRule {

	/* (non-Javadoc)
	 * @see ServidorAplicacao.strategy.enrolment.degree.rules.IEnrolmentRule#apply(ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext)
	 */
	public EnrolmentContext apply(EnrolmentContext enrolmentContext)
		throws ExcepcaoPersistencia {
		List precedenceList = null;
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IStudentCurricularPlan studentActiveCurricularPlan =
			enrolmentContext.getStudentActiveCurricularPlan();

		IPersistentPrecedence precedenceDAO = sp.getIPersistentPrecedence();

		precedenceList =
			precedenceDAO.readByDegreeCurricularPlan(
				studentActiveCurricularPlan.getDegreeCurricularPlan());

		List curricularCoursesToStay = new ArrayList();

		List finalCurricularCourseSpan =
			enrolmentContext.getFinalCurricularCoursesSpanToBeEnrolled();

		for (int i = 0; i < precedenceList.size(); i++) {
			IPrecedence precedence = (IPrecedence) precedenceList.get(i);
			if (precedence.evaluate(enrolmentContext)) {
				ICurricularCourse curricularCourse =
					precedence.getCurricularCourse();
				if (!(curricularCoursesToStay.contains(curricularCourse))
					&& (finalCurricularCourseSpan.contains(curricularCourse))) {
					curricularCoursesToStay.add(curricularCourse);
				}
			}
		}

		enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(
			curricularCoursesToStay);
		return enrolmentContext;
	}

}
