package Dominio;

import java.util.Date;
import java.util.List;

import Util.AreaType;
import Util.DegreeCurricularPlanState;
import Util.MarkType;
import Util.enrollment.EnrollmentRuleType;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public interface IDegreeCurricularPlan extends IDomainObject {

	public String getName();
	public Integer getIdInternal();
	public ICurso getDegree();
	public DegreeCurricularPlanState getState();
	public Date getEndDate();
	public Date getInitialDate();
	List getCurricularCourses();
	public Integer getDegreeDuration();
	public Integer getMinimalYearForOptionalCourses();
	public Double getNeededCredits();
	public MarkType getMarkType();
	public Integer getNumerusClausus();
	public String getDescription();
	public String getDescriptionEn();
	public String getEnrollmentStrategyClassName();
	public List getAreas();
	
	public void setDegreeDuration(Integer integer);
	public void setMinimalYearForOptionalCourses(Integer integer);
	public void setName(String name);
	public void setDegree(ICurso curso);
	public void setState(DegreeCurricularPlanState state);
	public void setEndDate(Date endDate);
	public void setInitialDate(Date initialDate);
	void setCurricularCourses(List curricularCourses);	
	public void setNeededCredits(Double neededCredits);
	public void setMarkType(MarkType markType);
	public void setNumerusClausus(Integer numerusClausus);
	public void setDescription(String description);
	public void setDescriptionEn(String descriptionEn);
	public void setEnrollmentStrategyClassName(String enrollmentStrategyClassName);
	public void setAreas(List areas);

	// -------------------------------------------------------------
	// BEGIN: Only for enrollment purposes
	// -------------------------------------------------------------
	public List getListOfEnrollmentRules(EnrollmentRuleType enrollmentRuleType);
	public List getCurricularCoursesFromArea(IBranch area, AreaType areaType);
	public List getCommonAreas();
	// -------------------------------------------------------------
	// END: Only for enrollment purposes
	// -------------------------------------------------------------
}