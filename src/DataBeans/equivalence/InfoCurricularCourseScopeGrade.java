package DataBeans.equivalence;

import DataBeans.InfoCurricularCourseScope;

public class InfoCurricularCourseScopeGrade {

	private InfoCurricularCourseScope infoCurricularCourseScope;
	private String grade;

	public InfoCurricularCourseScopeGrade() {
	}

	/**
	 * @return
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @return
	 */
	public InfoCurricularCourseScope getInfoCurricularCourseScope() {
		return infoCurricularCourseScope;
	}

	/**
	 * @param string
	 */
	public void setGrade(String string) {
		grade = string;
	}

	/**
	 * @param scope
	 */
	public void setInfoCurricularCourseScope(InfoCurricularCourseScope scope) {
		infoCurricularCourseScope = scope;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "infoCurricularCourseScope = " + this.infoCurricularCourseScope + "; ";
		result += "grade = " + this.grade + "]\n";
		return result;
	}
}