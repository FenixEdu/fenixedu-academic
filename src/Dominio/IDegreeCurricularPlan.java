package Dominio;

import java.util.Date;
import java.util.List;

import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public interface IDegreeCurricularPlan {

	public String getName();
	public Integer getIdInternal();
	public ICurso getDegree();
	public DegreeCurricularPlanState getState();
	public Date getEndDate();
	public Date getInitialDate();
	List getCurricularCourses();
	public Integer getDegreeDuration();
	public Integer getMinimalYearForOptionalCourses();

	public void setDegreeDuration(Integer integer);
	public void setMinimalYearForOptionalCourses(Integer integer);
	public void setName(String name);
	public void setDegree(ICurso curso);
	public void setState(DegreeCurricularPlanState state);
	public void setEndDate(Date endDate);
	public void setInitialDate(Date initialDate);
	void setCurricularCourses(List curricularCourses);	

}