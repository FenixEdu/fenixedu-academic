/*
 * InfoExecutionCourse.java
 *
 * Created on 28 de Novembro de 2002, 3:41
 */

package DataBeans;

import java.io.Serializable;

/**
 *
 * @author  tfc130
 */
public class InfoCurricularCourseScope implements Serializable {
	private InfoCurricularCourse infoCurricularCourse;
	private InfoCurricularSemester infoCurricularSemester;
	private InfoBranch infoBranch;


	public InfoCurricularCourseScope() {}


	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoCurricularCourseScope) {
			InfoCurricularCourseScope infoCurricularCourse = (InfoCurricularCourseScope) obj;
			resultado =	(getInfoBranch().equals(infoCurricularCourse.getInfoBranch())
					    && getInfoCurricularCourse().equals(infoCurricularCourse.getInfoCurricularCourse())
					    && getInfoCurricularSemester().equals(infoCurricularCourse.getInfoCurricularSemester()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "CurricularCourse = " + this.infoCurricularCourse + "; ";
		result += "CurricularSemester = " + this.infoCurricularSemester+ "]";
		result += "Branch = " + this.infoBranch + "]\n";
		
		return result;
	}
	/**
	 * @return InfoBranch
	 */
	public InfoBranch getInfoBranch() {
		return infoBranch;
	}

	/**
	 * @return InfoCurricularCourse
	 */
	public InfoCurricularCourse getInfoCurricularCourse() {
		return infoCurricularCourse;
	}

	/**
	 * @return InfoCurricularSemester
	 */
	public InfoCurricularSemester getInfoCurricularSemester() {
		return infoCurricularSemester;
	}

	/**
	 * Sets the infoBranch.
	 * @param infoBranch The infoBranch to set
	 */
	public void setInfoBranch(InfoBranch infoBranch) {
		this.infoBranch = infoBranch;
	}

	/**
	 * Sets the infoCurricularCourse.
	 * @param infoCurricularCourse The infoCurricularCourse to set
	 */
	public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
		this.infoCurricularCourse = infoCurricularCourse;
	}

	/**
	 * Sets the infoCurricularSemester.
	 * @param infoCurricularSemester The infoCurricularSemester to set
	 */
	public void setInfoCurricularSemester(InfoCurricularSemester infoCurricularSemester) {
		this.infoCurricularSemester = infoCurricularSemester;
	}

}
