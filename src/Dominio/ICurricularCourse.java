package Dominio;

import java.util.List;

import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;

/**
 * @author David Santos on Jul 12, 2004
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
	public void setEctsCredits(Double ectsCredits);
	public void setEnrollmentWeigth(Integer enrollmentWeigth);
	public void setMaximumValueForAcumulatedEnrollments(Integer maximumValueForAcumulatedEnrollments);
	public void setMinimumValueForAcumulatedEnrollments(Integer minimumValueForAcumulatedEnrollments);
	public void setWeigth(Double weigth);
	public void setMandatoryEnrollment(Boolean mandatoryEnrollment);

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
	public Boolean getBasic();
	public IScientificArea getScientificArea();
	public Double getEctsCredits();
	public Double getWeigth();
	public Boolean getMandatoryEnrollment();

	public boolean curricularCourseIsMandatory();

	// -------------------------------------------------------------
	// BEGIN: Only for enrollment purposes
	// -------------------------------------------------------------
	public Integer getEnrollmentWeigth();
	public Integer getMaximumValueForAcumulatedEnrollments();
	public Integer getMinimumValueForAcumulatedEnrollments();
	public ICurricularYear getCurricularYearByBranchAndSemester(IBranch branch, Integer semester);
	public String getCurricularCourseUniqueKeyForEnrollment();
	public boolean hasActiveScopeInGivenSemester(final Integer semester);
    public Boolean getEnrollmentAllowed();
    public void setEnrollmentAllowed(Boolean enrollmentAllowed);
	// -------------------------------------------------------------
	// END: Only for enrollment purposes
	// -------------------------------------------------------------

}