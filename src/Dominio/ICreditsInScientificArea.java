package Dominio;

/**
 * @author David Santos
 * Jan 14, 2004
 */

public interface ICreditsInScientificArea
{
	public IEnrolment getEnrolment();
	public void setEnrolment(IEnrolment enrolment);
	public Integer getEnrolmentKey();
	public void setEnrolmentKey(Integer enrolmentKey);
	public Integer getGivenCredits();
	public void setGivenCredits(Integer givenCredits);
	public IScientificArea getScientificArea();
	public void setScientificArea(IScientificArea scientificArea);
	public Integer getScientificAreaKey();
	public void setScientificAreaKey(Integer scientificAreaKey);
	public IStudentCurricularPlan getStudentCurricularPlan();
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
	public Integer getStudentCurricularPlanKey();
	public void setStudentCurricularPlanKey(Integer studentCurricularPlanKey);
}