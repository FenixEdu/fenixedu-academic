package Dominio.precedences;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionByNumberOfDoneCurricularCourses extends RestrictionByNumberOfCurricularCourses implements
	IRestrictionByNumberOfCurricularCourses
{
	public RestrictionByNumberOfDoneCurricularCourses()
	{
		super();
	}

	public boolean evaluate(PrecedenceContext precedenceContext)
	{
		return (precedenceContext.getStudentCurricularPlan().getNumberOfApprovedCurricularCourses() >= numberOfCurricularCourses
			.intValue());
	}
}