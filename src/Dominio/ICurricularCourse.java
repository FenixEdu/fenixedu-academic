package Dominio;

import java.util.List;

import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public interface ICurricularCourse extends IDomainObject {

	public void setCredits(Double credits);
	
	/**
	 * @deprecated
	 * @param theoreticalHours
	 */
	public void setTheoreticalHours(Double theoreticalHours);
	/**
	 * @deprecated
	 * @param theoreticalHours
	 */
	public void setPraticalHours(Double praticalHours);
	/**
	 * @deprecated
	 * @param theoreticalHours
	 */
	public void setTheoPratHours(Double theoPratHours);
	/**
	 * @deprecated
	 * @param theoreticalHours
	 */
	public void setLabHours(Double labHours);
	public void setName(java.lang.String name);
	public void setCode(java.lang.String code);
	public void setDepartmentCourse(IDisciplinaDepartamento departmentCourse);
	public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan);
	public void setAssociatedExecutionCourses(List associatedExecutionCourses);
	public void setScopes(List scopes);
	public void setType(CurricularCourseType type);
	public void setCurricularCourseExecutionScope(CurricularCourseExecutionScope scope);
	public void setMandatory(Boolean boolean1);
	public void setUniversity(IUniversity code);
	public void setBasic(Boolean basic);
	
	public void setScientificArea(IScientificArea scientificArea);
	
		
	
	public boolean curricularCourseIsMandatory();

	public Double getCredits();
	/**
	 * @deprecated
	 * @param theoreticalHours
	 */
	public Double getTheoreticalHours();
	/**
	 * @deprecated
	 * @param theoreticalHours
	 */
	public Double getPraticalHours();
	/**
	 * @deprecated
	 * @param theoreticalHours
	 */
	public Double getTheoPratHours();
	/**
	 * @deprecated
	 * @param theoreticalHours
	 */
	public Double getLabHours();
	public java.lang.String getName();
	public java.lang.String getCode();
	public IDisciplinaDepartamento getDepartmentCourse();
	public IDegreeCurricularPlan getDegreeCurricularPlan();
	public List getAssociatedExecutionCourses();
	public List getScopes();
	public CurricularCourseType getType();
	public CurricularCourseExecutionScope getCurricularCourseExecutionScope();
	public Boolean getMandatory();
	public IUniversity getUniversity();
	public ICurricularCourseScope getCurricularCourseScope(IBranch branch, Integer curricularSemester);
	public Boolean getBasic();
	
	public IScientificArea getScientificArea();
}