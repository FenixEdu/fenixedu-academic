package Dominio.precedences;


/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionNotDoneCurricularCourse extends RestrictionByCurricularCourse implements IRestrictionByCurricularCourse
{
	public RestrictionNotDoneCurricularCourse()
	{
		super();
	}

	public boolean evaluate(PrecedenceContext precedenceContext)
	{
		return !RestrictionDoneCurricularCourse.isCurricularCourseDone(this.getPrecedentCurricularCourse(),
			precedenceContext.getStudentApprovedEnrollments());
	}
}