/*
 * InfoExecutionCourse.java
 *
 * Created on 28 de Novembro de 2002, 3:41
 */

package DataBeans;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author  tfc130
 */
public class InfoCurricularCourseScope extends InfoObject implements Serializable {
	private InfoCurricularCourse infoCurricularCourse;
	private InfoCurricularSemester infoCurricularSemester;
	private InfoBranch infoBranch;

	private Integer maxIncrementNac;
	private Integer minIncrementNac;
	private Integer weigth;

	private Double theoreticalHours;
	private Double praticalHours;
	private Double theoPratHours;
	private Double labHours;
	private Double credits;
	private Double ectsCredits;
	
	private Calendar beginDate;
	private Calendar endDate;

	public InfoCurricularCourseScope() {
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoCurricularCourseScope) {
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) obj;
			resultado =
				(getInfoBranch().equals(infoCurricularCourseScope.getInfoBranch())
					&& getInfoCurricularCourse().equals(infoCurricularCourseScope.getInfoCurricularCourse())
					&& getInfoCurricularSemester().equals(infoCurricularCourseScope.getInfoCurricularSemester())
					&& ((getEndDate() == null && infoCurricularCourseScope.getEndDate() == null)
						|| (getEndDate() != null && infoCurricularCourseScope.getEndDate() != null && getEndDate().equals(infoCurricularCourseScope.getEndDate()))));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "CurricularCourse = " + this.infoCurricularCourse + "; ";
		result += "CurricularSemester = " + this.infoCurricularSemester + "; ";
		result += "Branch = " + this.infoBranch + "; ";
		result += "EndDate = " + this.endDate + "]\n";

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

	public Integer getMaxIncrementNac() {
		return maxIncrementNac;
	}

	public Integer getMinIncrementNac() {
		return minIncrementNac;
	}

	public Integer getWeigth() {
		return weigth;
	}

	public void setMaxIncrementNac(Integer integer) {
		maxIncrementNac = integer;
	}

	public void setMinIncrementNac(Integer integer) {
		minIncrementNac = integer;
	}

	public void setWeigth(Integer integer) {
		weigth = integer;
	}
	public Double getCredits() {
		return credits;
	}

	public Double getLabHours() {
		return labHours;
	}

	public Double getPraticalHours() {
		return praticalHours;
	}

	public Double getTheoPratHours() {
		return theoPratHours;
	}

	public Double getTheoreticalHours() {
		return theoreticalHours;
	}

	public void setCredits(Double double1) {
		credits = double1;
	}

	public void setLabHours(Double double1) {
		labHours = double1;
	}

	public void setPraticalHours(Double double1) {
		praticalHours = double1;
	}

	public void setTheoPratHours(Double double1) {
		theoPratHours = double1;
	}

	public void setTheoreticalHours(Double double1) {
		theoreticalHours = double1;
	}

	/**
	 * @return
	 */
	public Calendar getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate
	 */
	public void setBeginDate(Calendar beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return
	 */
	public Double getEctsCredits() {
		return ectsCredits;
	}

	/**
	 * @param ectsCredits
	 */
	public void setEctsCredits(Double ectsCredits) {
		this.ectsCredits = ectsCredits;
	}

	public Boolean isActive() {
		Boolean result = Boolean.FALSE;
		if(this.endDate == null) {
			result = Boolean.TRUE; 
		}
		return result;
	}

}