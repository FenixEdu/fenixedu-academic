package Dominio.precedences;


/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionDoneCurricularCourse extends RestrictionByCurricularCourse implements IRestrictionByCurricularCourse
{
	public RestrictionDoneCurricularCourse()
	{
		super();
	}

	public boolean evaluate(PrecedenceContext precedenceContext)
	{
		return precedenceContext.getStudentCurricularPlan().isCurricularCourseApproved(this.getPrecedentCurricularCourse());
	}

}