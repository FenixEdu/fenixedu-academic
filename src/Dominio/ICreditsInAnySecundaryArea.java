package Dominio;

/**
 * @author David Santos
 * Jan 14, 2004
 */

public interface ICreditsInAnySecundaryArea
{
	public Enrolment getEnrolment();
	public void setEnrolment(Enrolment enrolment);
	public Integer getEnrolmentKey();
	public void setEnrolmentKey(Integer enrolmentKey);
	public Integer getGivenCredits();
	public void setGivenCredits(Integer givenCredits);
	public StudentCurricularPlan getStudentCurricularPlan();
	public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan);
	public Integer getStudentCurricularPlanKey();
	public void setStudentCurricularPlanKey(Integer studentCurricularPlanKey);
}