package Dominio;

import java.util.List;

import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public interface ICurricularCourse {

	public void setCredits(Double credits);
	public void setTheoreticalHours(Double theoreticalHours);
	public void setPraticalHours(Double praticalHours);
	public void setTheoPratHours(Double theoPratHours);
	public void setLabHours(Double labHours);
	public void setName(java.lang.String name);
	public void setCode(java.lang.String code);
	public void setDepartmentCourse(IDisciplinaDepartamento departmentCourse);
	public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan);
	public void setAssociatedExecutionCourses(List associatedExecutionCourses);
//	public void setAssociatedBranches(List associatedBranches);
//	public void setAssociatedCurricularSemesters(List associatedCurricularSemesters);
	public void setScopes(List scopes);
	public void setType(CurricularCourseType type);

	public Double getCredits();
	public Double getTheoreticalHours();
	public Double getPraticalHours();
	public Double getTheoPratHours();
	public Double getLabHours();
	public java.lang.String getName();
	public java.lang.String getCode();
	public IDisciplinaDepartamento getDepartmentCourse();
	public IDegreeCurricularPlan getDegreeCurricularPlan();
	public List getAssociatedExecutionCourses();
//	public List getAssociatedBranches();
//	public List getAssociatedCurricularSemesters();
	public List getScopes();
	public CurricularCourseType getType();

}