package Dominio;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class PossibleCurricularCourseForOptionalCurricularCourse extends DomainObject implements IPossibleCurricularCourseForOptionalCurricularCourse {

	private ICurricularCourse optionalCurricularCourse;
	private ICurricularCourse possibleCurricularCourse;

	private Integer optionalCurricularCourseKey;
	private Integer possibleCurricularCourseKey;

	public PossibleCurricularCourseForOptionalCurricularCourse() {
	}

	public PossibleCurricularCourseForOptionalCurricularCourse(ICurricularCourse optionalCurricularCourse, ICurricularCourse chosenCurricularCourse) {
		this();
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof IPossibleCurricularCourseForOptionalCurricularCourse) {
			IPossibleCurricularCourseForOptionalCurricularCourse enrolment = (IPossibleCurricularCourseForOptionalCurricularCourse) obj;
			resultado = this.getOptionalCurricularCourse().equals(enrolment.getOptionalCurricularCourse()) &&
						this.getPossibleCurricularCourse().equals(enrolment.getPossibleCurricularCourse());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "optionalCurricularCourse = " + this.optionalCurricularCourse + "; ";
		result += "possibleCurricularCourse = " + this.possibleCurricularCourse + "]\n";
		return result;
	}

	/**
	 * @return
	 */
	public ICurricularCourse getPossibleCurricularCourse() {
		return possibleCurricularCourse;
	}

	/**
	 * @return
	 */
	public Integer getPossibleCurricularCourseKey() {
		return possibleCurricularCourseKey;
	}

	/**
	 * @return
	 */
	public ICurricularCourse getOptionalCurricularCourse() {
		return optionalCurricularCourse;
	}

	/**
	 * @return
	 */
	public Integer getOptionalCurricularCourseKey() {
		return optionalCurricularCourseKey;
	}

	/**
	 * @param course
	 */
	public void setPossibleCurricularCourse(ICurricularCourse course) {
		possibleCurricularCourse = course;
	}

	/**
	 * @param integer
	 */
	public void setPossibleCurricularCourseKey(Integer integer) {
		possibleCurricularCourseKey = integer;
	}

	/**
	 * @param course
	 */
	public void setOptionalCurricularCourse(ICurricularCourse course) {
		optionalCurricularCourse = course;
	}

	/**
	 * @param integer
	 */
	public void setOptionalCurricularCourseKey(Integer integer) {
		optionalCurricularCourseKey = integer;
	}

}