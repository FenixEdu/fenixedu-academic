package Dominio;


/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class EnrolmentInOptionalCurricularCourse extends Enrolment implements IEnrolmentInOptionalCurricularCourse
{
	private Integer curricularCourseForOptionKey;
	private ICurricularCourse curricularCourseForOption;

	public EnrolmentInOptionalCurricularCourse()
	{
		super();
	}

	/**
	 * @return Returns the curricularCourseForOption.
	 */
	public ICurricularCourse getCurricularCourseForOption()
	{
		return curricularCourseForOption;
	}

	/**
	 * @param curricularCourseForOption The curricularCourseForOption to set.
	 */
	public void setCurricularCourseForOption(ICurricularCourse curricularCourseForOption)
	{
		this.curricularCourseForOption = curricularCourseForOption;
	}

	/**
	 * @return Returns the curricularCourseForOptionKey.
	 */
	public Integer getCurricularCourseForOptionKey()
	{
		return curricularCourseForOptionKey;
	}

	/**
	 * @param curricularCourseForOptionKey The curricularCourseForOptionKey to set.
	 */
	public void setCurricularCourseForOptionKey(Integer curricularCourseForOptionKey)
	{
		this.curricularCourseForOptionKey = curricularCourseForOptionKey;
	}

	/* (non-Javadoc)
	 * @see Dominio.IEnrolment#getRealCurricularCourse()
	 */
	public ICurricularCourse getRealCurricularCourse()
	{
		return this.getCurricularCourseForOption();
	}

	public String toString() {
		String result = super.toString();
		result += "curricularCourseForOption = " + this.getCurricularCourseForOption() + "; ";
		return result;
	}

	public boolean equals(Object obj)
	{
		boolean result = false;

		if (obj instanceof IEnrolment) {
			IEnrolmentInOptionalCurricularCourse enrolment = (IEnrolmentInOptionalCurricularCourse) obj;
			
			result = super.equals(enrolment) && this.getCurricularCourseForOption().equals(enrolment.getCurricularCourseForOption());
		}
		return result;
	}
}