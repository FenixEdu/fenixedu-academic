package DataBeans.equivalence;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;

public class InfoCurricularCourseGrade {

	private InfoCurricularCourseScope infoCurricularCourseScope;
	private InfoCurricularCourse infoCurricularCourse;
	private String grade;

    /**
     * @return
     */
    public InfoCurricularCourse getInfoCurricularCourse()
    {
        return infoCurricularCourse;
    }

    /**
     * @param infoCurricularCourse
     */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse)
    {
        this.infoCurricularCourse = infoCurricularCourse;
    }

	public InfoCurricularCourseGrade() {
	}

	/**
	 * @return
	 */
	public String getGrade() {
		return grade;
	}

	/**
     * @deprecated
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
     * @deprecated
	 * @param scope
	 */
	public void setInfoCurricularCourseScope(InfoCurricularCourseScope scope) {
		infoCurricularCourseScope = scope;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "infoCurricularCourse = " + this.infoCurricularCourse + "; ";
		result += "grade = " + this.grade + "]\n";
		return result;
	}
}