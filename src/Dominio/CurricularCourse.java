/*
 * CurricularCourse.java
 *
 * Created on 28 of December 2002, 11:13
 */

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package Dominio;

import java.util.List;

public class CurricularCourse implements ICurricularCourse{

    private Integer internalCode;
    private Integer departmentCourseKey;
    private Integer degreeCurricularPlanKey;
    private Double credits;
    private Double theoreticalHours;
    private Double praticalHours;
    private Double theoPratHours;
    private Double labHours;
    private Integer curricularYear;
    private Integer semester;
    private String name;
    private String code;
    private IDisciplinaDepartamento departmentCourse;
    private IDegreeCurricularPlan degreeCurricularPlan;
    
    private List associatedExecutionCourses = null;
    
    /* Construtores */
    
    public CurricularCourse() {
    //	setDepartmentCourseKey(new Integer(-1));
    }

    public CurricularCourse(Double credits, Double theoreticalHours, Double praticalHours,
     Double theoPratHours, Double labHours, Integer curricularYear, Integer semester, String name,
     String code, IDisciplinaDepartamento departmentCourse, IDegreeCurricularPlan degreeCurricularPlan) {
        this();
        this.credits = credits;
        this.theoreticalHours = theoreticalHours;
        this.praticalHours = praticalHours;
        this.theoPratHours = theoPratHours;
        this.labHours = labHours;
        this.curricularYear = curricularYear;
        this.semester = semester;
        this.name = name;
        this.code = code;
        this.departmentCourse = departmentCourse;
        this.degreeCurricularPlan = degreeCurricularPlan;
		
        
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof CurricularCourse ) {
            CurricularCourse dc = (CurricularCourse)obj;
            resultado = 
            (getDepartmentCourseKey().equals(dc.getDepartmentCourseKey())) &&
            (getDegreeCurricularPlanKey().equals(dc.getDegreeCurricularPlanKey())) &&
            (getTheoreticalHours().equals(dc.getTheoreticalHours())) &&
            (getPraticalHours().equals(dc.getPraticalHours())) &&
            (getTheoPratHours().equals(dc.getTheoPratHours())) &&
            (getLabHours().equals(dc.getLabHours())) &&
            (getCurricularYear().equals(dc.getCurricularYear())) &&
            (getSemester().equals(dc.getSemester())) &&
            (getName().equals(dc.getName())) &&
            (getCode().equals(dc.getCode()));
        }
        return resultado;
    }

  public String toString() {
    String result = "[CURRICULAR_COURSE";
    result += ", Internal Code=" + internalCode;
    result += ", Credits=" + credits;
    result += ", Theoretical Hours=" + theoreticalHours;
    result += ", PraticalHours=" + praticalHours;
    result += ", Theoretical-Pratical Hours=" + theoPratHours;
    result += ", Lab Hours=" + labHours;
    result += ", Curricular Year=" + curricularYear;
    result += ", Semester=" + semester;
    result += ", Name=" + name;
    result += ", Code=" + code;
//    result += ", Department Course=" + departmentCourse;
//    result += ", Degree Curricular Plan=" + degreeCurricularPlan;
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
	 * Returns the credits.
	 * @return double
	 */
	public Double getCredits() {
		return credits;
	}

	/**
	 * Returns the curricularYear.
	 * @return int
	 * @deprecated
	 */
	public Integer getCurricularYear() {
		return curricularYear;
	}

	/**
	 * Returns the degreeCurricularPlan.
	 * @return IDegreeCurricularPlan
	 */
	public IDegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * Returns the degreeCurricularPlanKey.
	 * @return int
	 */
	public Integer getDegreeCurricularPlanKey() {
		return degreeCurricularPlanKey;
	}

	/**
	 * Returns the departmentCourse.
	 * @return IDisciplinaDepartamento
	 */
	public IDisciplinaDepartamento getDepartmentCourse() {
		return departmentCourse;
	}

	/**
	 * Returns the departmentCourseKey.
	 * @return int
	 */
	public Integer getDepartmentCourseKey() {
		return departmentCourseKey;
	}

	/**
	 * Returns the internalCode.
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * Returns the labHours.
	 * @return double
	 */
	public Double getLabHours() {
		return labHours;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the praticalHours.
	 * @return double
	 */
	public Double getPraticalHours() {
		return praticalHours;
	}

	/**
	 * Returns the semester.
	 * @return int
	 * @deprecated
	 */
	public Integer getSemester() {
		return semester;
	}

	/**
	 * Returns the theoPratHours.
	 * @return double
	 */
	public Double getTheoPratHours() {
		return theoPratHours;
	}

	/**
	 * Returns the theoreticalHours.
	 * @return double
	 */
	public Double getTheoreticalHours() {
		return theoreticalHours;
	}

	/**
	 * Sets the code.
	 * @param code The code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

//	/**
//	 * Sets the credits.
//	 * @param credits The credits to set
//	 */
//	public void setCredits(double credits) {
//		this.credits = credits;
//	}

	/**
	 * Sets the curricularYear.
	 * @param curricularYear The curricularYear to set
	 * @deprecated
	 */
	public void setCurricularYear(Integer curricularYear) {
		this.curricularYear = curricularYear;
	}

	/**
	 * Sets the degreeCurricularPlan.
	 * @param degreeCurricularPlan The degreeCurricularPlan to set
	 */
	public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
		this.degreeCurricularPlan = degreeCurricularPlan;
	}

	/**
	 * Sets the degreeCurricularPlanKey.
	 * @param degreeCurricularPlanKey The degreeCurricularPlanKey to set
	 */
	public void setDegreeCurricularPlanKey(Integer degreeCurricularPlanKey) {
		this.degreeCurricularPlanKey = degreeCurricularPlanKey;
	}

	/**
	 * Sets the departmentCourse.
	 * @param departmentCourse The departmentCourse to set
	 */
	public void setDepartmentCourse(IDisciplinaDepartamento departmentCourse) {
		this.departmentCourse = departmentCourse;
	}

	/**
	 * Sets the departmentCourseKey.
	 * @param departmentCourseKey The departmentCourseKey to set
	 */
	public void setDepartmentCourseKey(Integer departmentCourseKey) {
		this.departmentCourseKey = departmentCourseKey;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

//	/**
//	 * Sets the labHours.
//	 * @param labHours The labHours to set
//	 */
//	public void setLabHours(double labHours) {
//		this.labHours = labHours;
//	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

//	/**
//	 * Sets the praticalHours.
//	 * @param praticalHours The praticalHours to set
//	 */
//	public void setPraticalHours(double praticalHours) {
//		this.praticalHours = praticalHours;
//	}

	/**
	 * Sets the semester.
	 * @param semester The semester to set
	 * @deprecated
	 */
	public void setSemester(Integer semester) {
		this.semester = semester;
	}

//	/**
//	 * Sets the theoPratHours.
//	 * @param theoPratHours The theoPratHours to set
//	 */
//	public void setTheoPratHours(double theoPratHours) {
//		this.theoPratHours = theoPratHours;
//	}
//
//	/**
//	 * Sets the theoreticalHours.
//	 * @param theoreticalHours The theoreticalHours to set
//	 */
//	public void setTheoreticalHours(double theoreticalHours) {
//		this.theoreticalHours = theoreticalHours;
//	}

	/**
	 * Returns the associatedExecutionCourses.
	 * @return List
	 */
	public List getAssociatedExecutionCourses() {
		return associatedExecutionCourses;
	}

	/**
	 * Sets the associatedExecutionCourses.
	 * @param associatedExecutionCourses The associatedExecutionCourses to set
	 */
	public void setAssociatedExecutionCourses(List associatedExecutionCourses) {
		this.associatedExecutionCourses = associatedExecutionCourses;
	}

	/**
	 * Sets the credits.
	 * @param credits The credits to set
	 */
	public void setCredits(Double credits) {
		this.credits = credits;
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

}