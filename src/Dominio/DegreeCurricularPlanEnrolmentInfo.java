package Dominio;


/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class DegreeCurricularPlanEnrolmentInfo implements IDegreeCurricularPlanEnrolmentInfo {

	private Integer degreeDuration;
	private Integer minimalYearForOptionalCourses;

	private Integer internalID;

	public DegreeCurricularPlanEnrolmentInfo() {
	}

	public DegreeCurricularPlanEnrolmentInfo(IDegreeCurricularPlan degreeCurricularPlan, Integer degreeDuration, Integer minimalYearForOptionalCourses) {
		this();
		setDegreeDuration(degreeDuration);
		setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IDegreeCurricularPlanEnrolmentInfo) {
			IDegreeCurricularPlanEnrolmentInfo degreeEnrolmentInfo = (IDegreeCurricularPlanEnrolmentInfo) obj;
			result = 	this.getDegreeDuration().equals(degreeEnrolmentInfo.getDegreeDuration()) &&
						this.getMinimalYearForOptionalCourses().equals(degreeEnrolmentInfo.getMinimalYearForOptionalCourses());
		}
		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "degreeDuration = " + this.degreeDuration + "; ";
		result += "minimalYearForOptionalCourses = " + this.minimalYearForOptionalCourses + "]\n";
		return result;
	}
	/**
	 * @return
	 */
	public Integer getDegreeDuration() {
		return degreeDuration;
	}

	/**
	 * @return
	 */
	public Integer getInternalID() {
		return internalID;
	}

	/**
	 * @return
	 */
	public Integer getMinimalYearForOptionalCourses() {
		return minimalYearForOptionalCourses;
	}

	/**
	 * @param integer
	 */
	public void setDegreeDuration(Integer integer) {
		degreeDuration = integer;
	}

	/**
	 * @param integer
	 */
	public void setInternalID(Integer integer) {
		internalID = integer;
	}

	/**
	 * @param integer
	 */
	public void setMinimalYearForOptionalCourses(Integer integer) {
		minimalYearForOptionalCourses = integer;
	}

}