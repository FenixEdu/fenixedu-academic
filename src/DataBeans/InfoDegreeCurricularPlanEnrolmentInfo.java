package DataBeans;

import java.io.Serializable;

/**
 * @author dcs-rjao
 *
 * 22/Abr/2003
 */
public class InfoDegreeCurricularPlanEnrolmentInfo implements Serializable {

	private Integer degreeDuration;
	private Integer minimalYearForOptionalCourses;

	public InfoDegreeCurricularPlanEnrolmentInfo() {
	}

	public InfoDegreeCurricularPlanEnrolmentInfo(
		Integer degreeDuration,
		Integer minimalYearForOptionalCourses) {
		this();
		setDegreeDuration(degreeDuration);
		setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoDegreeCurricularPlanEnrolmentInfo) {
			InfoDegreeCurricularPlanEnrolmentInfo infoDegreeEnrolmentInfo =
				(InfoDegreeCurricularPlanEnrolmentInfo) obj;
			result = 	this.getDegreeDuration().equals(infoDegreeEnrolmentInfo.getDegreeDuration()) &&
						this.getMinimalYearForOptionalCourses().equals(infoDegreeEnrolmentInfo.getMinimalYearForOptionalCourses());
		}
		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
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
	public void setMinimalYearForOptionalCourses(Integer integer) {
		minimalYearForOptionalCourses = integer;
	}
}
