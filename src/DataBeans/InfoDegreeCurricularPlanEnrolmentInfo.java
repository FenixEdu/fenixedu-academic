package DataBeans;

import java.io.Serializable;

/**
 * @author dcs-rjao
 *
 * 22/Abr/2003
 */
public class InfoDegreeCurricularPlanEnrolmentInfo implements Serializable {

	private InfoDegreeCurricularPlan infoDegreeCurricularPlan;
	private Integer degreeDuration;
	private Integer minimalYearForOptionalCourses;

	public InfoDegreeCurricularPlanEnrolmentInfo() {
	}

	public InfoDegreeCurricularPlanEnrolmentInfo(
		InfoDegreeCurricularPlan infoDegreeCurricularPlan,
		Integer degreeDuration,
		Integer minimalYearForOptionalCourses) {
		this();
		setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		setDegreeDuration(degreeDuration);
		setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoDegreeCurricularPlanEnrolmentInfo) {
			InfoDegreeCurricularPlanEnrolmentInfo infoDegreeEnrolmentInfo =
				(InfoDegreeCurricularPlanEnrolmentInfo) obj;
			result =
				this.getInfoDegreeCurricularPlan().equals(infoDegreeEnrolmentInfo.getInfoDegreeCurricularPlan());
		}
		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "infoDegreeCurricularPlan = " + this.infoDegreeCurricularPlan + "; ";
		result += "degreeDuration = " + this.degreeDuration + "; ";
		result += "minimalYearForOptionalCourses = "+ this.minimalYearForOptionalCourses + "]\n";
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
	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
		return infoDegreeCurricularPlan;
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
	 * @param plan
	 */
	public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan plan) {
		infoDegreeCurricularPlan = plan;
	}

	/**
	 * @param integer
	 */
	public void setMinimalYearForOptionalCourses(Integer integer) {
		minimalYearForOptionalCourses = integer;
	}
}
