package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import Util.PrecedenceScopeToApply;

/**
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public class EnrolmentApplyPrecedencesForOptionalCurricularCoursesRule /*extends EnrolmentPrecedenceRule implements IEnrolmentRule*/
{
	protected void doApply(StudentEnrolmentContext studentEnrolmentContext, List curricularCoursesToApply)
	{
//		List result = EnrolmentApplyPrecedencesRule.doIt(studentEnrolmentContext, curricularCoursesToApply, getScopeToApply());
//		studentEnrolmentContext.setFinalCurricularCoursesWhereStudentCanBeEnrolled(result);
	}

	protected PrecedenceScopeToApply getScopeToApply()
	{
		return PrecedenceScopeToApply.TO_APLLY_TO_OPTION_LIST;
	}

	/**
	 * @param enrolmentContext
	 * @return List to apply this rule
	 */
	protected List getListOfCurricularCoursesToApply(StudentEnrolmentContext studentEnrolmentContext)
	{
		// List optionalList =	enrolmentContext.getOptionalCurricularCoursesToChooseFromDegree();
		// FIXME [DAVID]: The apropriate list must be returned. This is not done yet because this rule is not used yet.
		return new ArrayList();
	}
}