package Dominio;


/**
 * @author David Santos
 * Jan 14, 2004
 */

public class CreditsInAnySecundaryArea extends DomainObject implements ICreditsInAnySecundaryArea
{
	private Integer studentCurricularPlanKey;
	private Integer enrolmentKey;

	private Integer givenCredits;
	private IStudentCurricularPlan studentCurricularPlan;
	private IEnrolment enrolment;

	public CreditsInAnySecundaryArea()
	{
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICreditsInAnySecundaryArea) {
			ICreditsInAnySecundaryArea creditsInSpecificScientificArea = (ICreditsInAnySecundaryArea) obj;
			result =
				this.getEnrolment().equals(creditsInSpecificScientificArea.getEnrolment()) &&
				this.getStudentCurricularPlan().equals(creditsInSpecificScientificArea.getStudentCurricularPlan());
		}
		return result;
	}

	public String toString() {
		String result = "student: [" + this.getStudentCurricularPlan().getStudent().getNumber().toString();
		result += "] course: [" + this.getEnrolment().getCurricularCourse().getName() + "]";
		return result;
	}

	/**
	 * @return Returns the enrolment.
	 */
	public IEnrolment getEnrolment()
	{
		return enrolment;
	}

	/**
	 * @param enrolment The enrolment to set.
	 */
	public void setEnrolment(IEnrolment enrolment)
	{
		this.enrolment = enrolment;
	}

	/**
	 * @return Returns the enrolmentKey.
	 */
	public Integer getEnrolmentKey()
	{
		return enrolmentKey;
	}

	/**
	 * @param enrolmentKey The enrolmentKey to set.
	 */
	public void setEnrolmentKey(Integer enrolmentKey)
	{
		this.enrolmentKey = enrolmentKey;
	}

	/**
	 * @return Returns the givenCredits.
	 */
	public Integer getGivenCredits()
	{
		return givenCredits;
	}

	/**
	 * @param givenCredits The givenCredits to set.
	 */
	public void setGivenCredits(Integer givenCredits)
	{
		this.givenCredits = givenCredits;
	}

	/**
	 * @return Returns the studentCurricularPlan.
	 */
	public IStudentCurricularPlan getStudentCurricularPlan()
	{
		return studentCurricularPlan;
	}

	/**
	 * @param studentCurricularPlan The studentCurricularPlan to set.
	 */
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
	{
		this.studentCurricularPlan = studentCurricularPlan;
	}

	/**
	 * @return Returns the studentCurricularPlanKey.
	 */
	public Integer getStudentCurricularPlanKey()
	{
		return studentCurricularPlanKey;
	}

	/**
	 * @param studentCurricularPlanKey The studentCurricularPlanKey to set.
	 */
	public void setStudentCurricularPlanKey(Integer studentCurricularPlanKey)
	{
		this.studentCurricularPlanKey = studentCurricularPlanKey;
	}
}