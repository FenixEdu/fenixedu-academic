package ServidorAplicacao.strategy.enrolment.rules;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import Util.PrecedenceScopeToApply;

/**
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public abstract class EnrolmentPrecedenceRule implements IEnrolmentRule
{
	public StudentEnrolmentContext apply(StudentEnrolmentContext studentEnrolmentContext)
	{
		doApply(studentEnrolmentContext);
		return studentEnrolmentContext;
	}

	/**
	 * @param studentEnrolmentContext
	 * @return
	 */
	abstract protected List getListOfCurricularCoursesToApply(StudentEnrolmentContext studentEnrolmentContext);

	/**
	 * @param studentEnrolmentContext
	 * @param precedenceList
	 * @param curricularCourseToApply
	 */
	abstract protected void doApply(
		StudentEnrolmentContext studentEnrolmentContext);

	/**
	 * Tells what PrecedenceScopeToAplly
	 * @see PrecedenceScopeToApply
	 * @return PrecedenceScopeToApply
	 */
	abstract protected PrecedenceScopeToApply getScopeToApply();
}
