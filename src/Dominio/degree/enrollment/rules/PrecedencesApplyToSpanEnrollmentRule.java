package Dominio.degree.enrollment.rules;

import java.util.List;
import java.util.Map;

import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import Dominio.precedences.PrecedenceContext;
import Util.PrecedenceScopeToApply;

/**
 * @author David Santos in Jun 9, 2004
 */

public class PrecedencesApplyToSpanEnrollmentRule extends PrecedencesEnrollmentRule implements IEnrollmentRule
{
	public PrecedencesApplyToSpanEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
												List curricularCoursesWhereStudentCanBeEnrolled,
												IExecutionPeriod executionPeriod, Map acumulatedEnrollments)
	{
		super.precedenceContext = new PrecedenceContext();
		super.precedenceContext.setAcumulatedEnrollments(acumulatedEnrollments);
		super.precedenceContext.setCurricularCoursesWhereStudentCanBeEnrolled(curricularCoursesWhereStudentCanBeEnrolled);
		super.precedenceContext.setExecutionPeriod(executionPeriod);
		super.precedenceContext.setStudentApprovedEnrollments(studentCurricularPlan.getStudentApprovedEnrollments());
		super.precedenceContext.setStudentEnrolledEnrollments(studentCurricularPlan.getStudentEnrolledEnrollments());
	}

	protected PrecedenceScopeToApply getScopeToApply()
	{
		return PrecedenceScopeToApply.TO_APPLY_TO_SPAN;
	}
}