package Dominio;

/**
 * @author David Santos
 * Jan 14, 2004
 */

public interface ICreditsInSpecificScientificArea
{
	public Enrolment getEnrolment();
	public void setEnrolment(Enrolment enrolment);
	public Integer getEnrolmentKey();
	public void setEnrolmentKey(Integer enrolmentKey);
	public Integer getGivenCredits();
	public void setGivenCredits(Integer givenCredits);
	public ScientificArea getScientificArea();
	public void setScientificArea(ScientificArea scientificArea);
	public Integer getScientificAreaKey();
	public void setScientificAreaKey(Integer scientificAreaKey);
	public StudentCurricularPlan getStudentCurricularPlan();
	public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan);
	public Integer getStudentCurricularPlanKey();
	public void setStudentCurricularPlanKey(Integer studentCurricularPlanKey);
}