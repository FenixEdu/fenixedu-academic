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
public class InfoCurricularCourse implements Serializable {
	private String name;
	private String code;
	private Double credits;
	private Double theoreticalHours;
	private Double praticalHours;
	private Double theoPratHours;
	private Double labHours;
	private Integer curricularYear;
	private Integer semester;
	private InfoDegreeCurricularPlan infoDegreeCurricularPlan;


	public InfoCurricularCourse() {}


	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoCurricularCourse) {
			InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) obj;
			resultado =	(getName().equals(infoCurricularCourse.getName())
					    && getCode().equals(infoCurricularCourse.getCode()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[INFODISCIPLINACURRICULAR";
		result += ", nome=" + name;
		result += ", sigla=" + code;
		result += "]";
		return result;
	}


	/**
	 * Returns the code.
	 * @return String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Returns the nome.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the code.
	 * @param code The code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Sets the nome.
	 * @param nome The nome to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the credits.
	 * @return Double
	 */
	public Double getCredits() {
		return credits;
	}

	/**
	 * Returns the curricularYear.
	 * @return Integer
	 */
	public Integer getCurricularYear() {
		return curricularYear;
	}

	/**
	 * Returns the labHours.
	 * @return Double
	 */
	public Double getLabHours() {
		return labHours;
	}

	/**
	 * Returns the praticalHours.
	 * @return Double
	 */
	public Double getPraticalHours() {
		return praticalHours;
	}

	/**
	 * Returns the semester.
	 * @return Integer
	 */
	public Integer getSemester() {
		return semester;
	}

	/**
	 * Returns the theoPratHours.
	 * @return Double
	 */
	public Double getTheoPratHours() {
		return theoPratHours;
	}

	/**
	 * Returns the theoreticalHours.
	 * @return Double
	 */
	public Double getTheoreticalHours() {
		return theoreticalHours;
	}

	/**
	 * Sets the credits.
	 * @param credits The credits to set
	 */
	public void setCredits(Double credits) {
		this.credits = credits;
	}

	/**
	 * Sets the curricularYear.
	 * @param curricularYear The curricularYear to set
	 */
	public void setCurricularYear(Integer curricularYear) {
		this.curricularYear = curricularYear;
	}

	/**
	 * Sets the labHours.
	 * @param labHours The labHours to set
	 */
	public void setLabHours(Double labHours) {
		this.labHours = labHours;
	}

	/**
	 * Sets the praticalHours.
	 * @param praticalHours The praticalHours to set
	 */
	public void setPraticalHours(Double praticalHours) {
		this.praticalHours = praticalHours;
	}

	/**
	 * Sets the semester.
	 * @param semester The semester to set
	 */
	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	/**
	 * Sets the theoPratHours.
	 * @param theoPratHours The theoPratHours to set
	 */
	public void setTheoPratHours(Double theoPratHours) {
		this.theoPratHours = theoPratHours;
	}

	/**
	 * Sets the theoreticalHours.
	 * @param theoreticalHours The theoreticalHours to set
	 */
	public void setTheoreticalHours(Double theoreticalHours) {
		this.theoreticalHours = theoreticalHours;
	}

	/**
	 * Returns the infoDegreeCurricularPlan.
	 * @return InfoDegreeCurricularPlan
	 */
	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
		return infoDegreeCurricularPlan;
	}

	/**
	 * Sets the infoDegreeCurricularPlan.
	 * @param infoDegreeCurricularPlan The infoDegreeCurricularPlan to set
	 */
	public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
	}

}
