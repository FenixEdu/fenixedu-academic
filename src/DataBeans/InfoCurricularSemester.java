package DataBeans;

import java.io.Serializable;
import java.util.List;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class InfoCurricularSemester implements Serializable {

	private Integer semester;
	private InfoCurricularYear infoCurricularYear;
	private List associatedInfoCurricularCourses;
	

	public InfoCurricularSemester() {
		setSemester(null);
		setAssociatedInfoCurricularCourses(null);
		setInfoCurricularYear(null);
	}

	public InfoCurricularSemester(Integer semester, InfoCurricularYear curricularYear) {
		setSemester(semester);
		setAssociatedInfoCurricularCourses(null);
		setInfoCurricularYear(curricularYear);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoCurricularSemester) {
			InfoCurricularSemester infoCurricularSemester = (InfoCurricularSemester) obj;
			resultado = (this.getSemester().equals(infoCurricularSemester.getSemester()) &&
									(this.getInfoCurricularYear().equals(infoCurricularSemester.getInfoCurricularYear())));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "semester = " + this.getSemester() + "; ";
		result += "infoCurricularYear = " + this.getInfoCurricularYear() + "]";
//		result += "associatedInfoCurricularCourses = " + this.associatedInfoCurricularCourses + "; ";
		return result;
	}

	/**
	 * @return List
	 */
	public List getAssociatedInfoCurricularCourses() {
		return associatedInfoCurricularCourses;
	}

	/**
	 * @return ICurricularYear
	 */
	public InfoCurricularYear getInfoCurricularYear() {
		return infoCurricularYear;
	}

	/**
	 * @return Integer
	 */
	public Integer getSemester() {
		return semester;
	}

	/**
	 * Sets the associatedInfoCurricularCourses.
	 * @param associatedInfoCurricularCourses The associatedInfoCurricularCourses to set
	 */
	public void setAssociatedInfoCurricularCourses(List associatedCurricularCourses) {
		this.associatedInfoCurricularCourses = associatedCurricularCourses;
	}

	/**
	 * Sets the infoCurricularYear.
	 * @param infoCurricularYear The infoCurricularYear to set
	 */
	public void setInfoCurricularYear(InfoCurricularYear curricularYear) {
		this.infoCurricularYear = curricularYear;
	}

	/**
	 * Sets the semester.
	 * @param semester The semester to set
	 */
	public void setSemester(Integer semester) {
		this.semester = semester;
	}

}