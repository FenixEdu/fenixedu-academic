/*
 * Created on 8/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
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
public class EnrolmentFilterPrecedenceRule implements IEnrolmentRule {

	/* (non-Javadoc)
	 * @see ServidorAplicacao.strategy.enrolment.degree.rules.IEnrolmentRule#apply(ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext)
	 */
	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		List precedenceList = null;


		IStudentCurricularPlan studentActiveCurricularPlan =
			enrolmentContext.getStudentActiveCurricularPlan();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentPrecedence precedenceDAO = sp.getIPersistentPrecedence();
			precedenceList =
				precedenceDAO.readByDegreeCurricularPlan(
					studentActiveCurricularPlan.getDegreeCurricularPlan());
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			throw new IllegalStateException("Cannot read from database");
		}

		List curricularCourseScopesNotToStay = new ArrayList();

		List finalCurricularCourseScopesSpan =
			enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
		for (int i = 0; i < precedenceList.size(); i++) {
			IPrecedence precedence = (IPrecedence) precedenceList.get(i);
			if (!precedence.evaluate(enrolmentContext)) {
				ICurricularCourse curricularCourse = precedence.getCurricularCourse();
				List scopes = curricularCourse.getScopes();	
				for (int scopeIndex = 0; scopeIndex < scopes.size(); scopeIndex++){
					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) scopes.get(scopeIndex);
					if (!curricularCourseScopesNotToStay.contains(curricularCourseScope)) {
						curricularCourseScopesNotToStay.add(curricularCourseScope);
					}
				}
			}
		}

		enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().removeAll(curricularCourseScopesNotToStay);
		finalCurricularCourseScopesSpan.removeAll(curricularCourseScopesNotToStay);		

		return enrolmentContext;
	}
}
