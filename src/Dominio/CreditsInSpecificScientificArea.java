package Dominio;


/**
 * @author David Santos
 * Jan 14, 2004
 */

public class CreditsInSpecificScientificArea extends DomainObject implements ICreditsInSpecificScientificArea
{
	private Integer studentCurricularPlanKey;
	private Integer scientificAreaKey;
	private Integer enrolmentKey;

	private Integer givenCredits;
	private StudentCurricularPlan studentCurricularPlan;
	private ScientificArea scientificArea;
	private Enrolment enrolment;

	public CreditsInSpecificScientificArea()
	{
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICreditsInSpecificScientificArea) {
			ICreditsInSpecificScientificArea creditsInSpecificScientificArea = (ICreditsInSpecificScientificArea) obj;
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
		result += "] course: [" + this.getEnrolment().getCurricularCourseScope().getCurricularCourse().getName() + "]";
		return result;
	}

	/**
	 * @return Returns the enrolment.
	 */
	public Enrolment getEnrolment()
	{
		return enrolment;
	}

	/**
	 * @param enrolment The enrolment to set.
	 */
	public void setEnrolment(Enrolment enrolment)
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
	public ScientificArea getScientificArea()
	{
		return scientificArea;
	}

	/**
	 * @param scientificArea The scientificArea to set.
	 */
	public void setScientificArea(ScientificArea scientificArea)
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
	public StudentCurricularPlan getStudentCurricularPlan()
	{
		return studentCurricularPlan;
	}

	/**
	 * @param studentCurricularPlan The studentCurricularPlan to set.
	 */
	public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan)
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