package Dominio;


/**
 * @author David Santos
 * Jan 14, 2004
 */

public class CreditsInScientificArea extends DomainObject implements ICreditsInScientificArea
{
	private Integer studentCurricularPlanKey;
	private Integer scientificAreaKey;
	private Integer enrolmentKey;

	private Integer givenCredits;
	private IStudentCurricularPlan studentCurricularPlan;
	private IScientificArea scientificArea;
	private IEnrollment enrolment;

	public CreditsInScientificArea()
	{
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICreditsInScientificArea) {
			ICreditsInScientificArea creditsInSpecificScientificArea = (ICreditsInScientificArea) obj;
			result =
				this.getEnrolment().equals(creditsInSpecificScientificArea.getEnrolment()) &&
				this.getStudentCurricularPlan().equals(creditsInSpecificScientificArea.getStudentCurricularPlan()) &&
				this.getScientificArea().equals(creditsInSpecificScientificArea.getScientificArea());
		}
		return result;
	}

	public String toString() {
		String result = "scientificArea: [" + this.getScientificArea().getName();
		result += "] student: [" + this.getStudentCurricularPlan().getStudent().getNumber().toString();
		result += "] course: [" + this.getEnrolment().getCurricularCourse().getName() + "]";
		return result;
	}

	/**
	 * @return Returns the enrolment.
	 */
	public IEnrollment getEnrolment()
	{
		return enrolment;
	}

	/**
	 * @param enrolment The enrolment to set.
	 */
	public void setEnrolment(IEnrollment enrolment)
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
	 * @return Returns the scientificArea.
	 */
	public IScientificArea getScientificArea()
	{
		return scientificArea;
	}

	/**
	 * @param scientificArea The scientificArea to set.
	 */
	public void setScientificArea(IScientificArea scientificArea)
	{
		this.scientificArea = scientificArea;
	}

	/**
	 * @return Returns the scientificAreaKey.
	 */
	public Integer getScientificAreaKey()
	{
		return scientificAreaKey;
	}

	/**
	 * @param scientificAreaKey The scientificAreaKey to set.
	 */
	public void setScientificAreaKey(Integer scientificAreaKey)
	{
		this.scientificAreaKey = scientificAreaKey;
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