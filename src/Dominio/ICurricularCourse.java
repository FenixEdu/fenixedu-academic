/*
 * ICurricularCourse.java
 *
 * Created on 28 of December 2002, 11:32
 */

/**
 *
 * @author Nuno Nunes & Joana Mota
 */


package Dominio;

import java.util.List;


public interface ICurricularCourse {
    public void setCredits(Double credits);
    public void setTheoreticalHours(Double theoreticalHours);
    public void setPraticalHours(Double praticalHours);
    public void setTheoPratHours(Double theoPratHours);
    public void setLabHours(Double labHours);
    public void setSemester(Integer semester);
    public void setName(java.lang.String name);
    public void setCode(java.lang.String code);
	public void setDepartmentCourse(IDisciplinaDepartamento departmentCourse);
	public void setDegreeCurricularPlan(IPlanoCurricularCurso degreeCurricularPlan);
	public void setAssociatedExecutionCourses(List associatedExecutionCourses);
	public void setCurricularYear(Integer newCurricularYear);

    public Double getCredits();
    public Double getTheoreticalHours();
    public Double getPraticalHours();
    public Double getTheoPratHours();
    public Double getLabHours();
    public Integer getCurricularYear();
    public Integer getSemester();
    public java.lang.String getName();
    public java.lang.String getCode();
    public IDisciplinaDepartamento getDepartmentCourse();
    public IPlanoCurricularCurso getDegreeCurricularPlan();
    public List getAssociatedExecutionCourses();
}
