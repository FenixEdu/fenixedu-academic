package Dominio;


/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class DegreeEnrolmentInfo implements IDegreeEnrolmentInfo {

	private IDegreeCurricularPlan degreeCurricularPlan;
	private Integer degreeDuration;
	private Integer minimalYearForOptionalCourses;

	private Integer internalID;
	private Integer degreeCurricularPlanKey;

	public DegreeEnrolmentInfo() {
	}

	public DegreeEnrolmentInfo(IDegreeCurricularPlan degreeCurricularPlan, Integer degreeDuration, Integer minimalYearForOptionalCourses) {
		this();
		setDegreeCurricularPlan(degreeCurricularPlan);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IDegreeEnrolmentInfo) {
			IDegreeEnrolmentInfo degreeEnrolmentInfo = (IDegreeEnrolmentInfo) obj;
			result = this.getDegreeCurricularPlan().equals(degreeEnrolmentInfo.getDegreeCurricularPlan());
		}
		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "degreeCurricularPlan = " + this.degreeCurricularPlan + "; ";
		result += "degreeDuration = " + this.degreeDuration + "; ";
		result += "minimalYearForOptionalCourses = " + this.minimalYearForOptionalCourses + "]\n";
		return result;
	}
	/**
	 * @return
	 */
	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * @return
	 */
	public Integer getDegreeCurricularPlanKey() {
		return degreeCurricularPlanKey;
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
	 * @param plan
	 */
	public void setDegreeCurricularPlan(IDegreeCurricularPlan plan) {
		degreeCurricularPlan = plan;
	}

	/**
	 * @param integer
	 */
	public void setDegreeCurricularPlanKey(Integer integer) {
		degreeCurricularPlanKey = integer;
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