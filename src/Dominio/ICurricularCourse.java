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
	public void setTheoreticalHours(Double theoreticalHours);
	public void setPraticalHours(Double praticalHours);
	public void setTheoPratHours(Double theoPratHours);
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
	public List getScopes();
	public CurricularCourseType getType();
	public CurricularCourseExecutionScope getCurricularCourseExecutionScope();
	public Boolean getMandatory();
	public IUniversity getUniversity();
	public ICurricularCourseScope getCurricularCourseScope(IBranch branch, Integer curricularSemester);
	public Boolean getBasic();
	public IScientificArea getScientificArea();

	public Double getEctsCredits();
	public void setEctsCredits(Double ectsCredits);
	public Integer getEnrollmentWeigth();
	public void setEnrollmentWeigth(Integer enrollmentWeigth);
	public Integer getMaximumValueForAcumulatedEnrollments();
	public void setMaximumValueForAcumulatedEnrollments(Integer maximumValueForAcumulatedEnrollments);
	public Integer getMinimumValueForAcumulatedEnrollments();
	public void setMinimumValueForAcumulatedEnrollments(Integer minimumValueForAcumulatedEnrollments);
	public ICurricularYear getCurricularYearByBranch(IBranch branch);

	public boolean curricularCourseIsMandatory();

	public Double getWeigth();
	public void setWeigth(Double weigth);
	public Boolean getMandatoryEnrollment();
	public void setMandatoryEnrollment(Boolean mandatoryEnrollment);
}