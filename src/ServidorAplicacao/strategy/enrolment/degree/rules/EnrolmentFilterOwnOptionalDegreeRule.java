package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.List;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentFilterOwnOptionalDegreeRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		List degreesList = new ArrayList();
		degreesList.add(enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan().getDegree());

		enrolmentContext.setDegreesForOptionalCurricularCourses(degreesList);
		return enrolmentContext;
	}
}