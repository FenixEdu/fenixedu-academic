package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentFilterRestrictedOptionalDegreeRule //implements IEnrolmentRule
{

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		List degreesList = new ArrayList();
		degreesList.add(enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan().getDegree());

		enrolmentContext.setDegreesForOptionalCurricularCourses(degreesList);
		return enrolmentContext;
	}
}