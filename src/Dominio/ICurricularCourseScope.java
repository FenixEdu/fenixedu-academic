package Dominio;

import java.util.Calendar;



/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public interface ICurricularCourseScope extends IDomainObject {
	public IBranch getBranch();
	public ICurricularCourse getCurricularCourse();
	public ICurricularSemester getCurricularSemester();
	public Integer getMaxIncrementNac();
	public Integer getMinIncrementNac();
	public Integer getWeigth();
	public Double getLabHours();
	public Double getPraticalHours();
	public Double getTheoPratHours();
	public Double getTheoreticalHours();
	public Double getCredits();
	public Double getEctsCredits();
	public Calendar getBeginDate();
	public Calendar getEndDate();
//	public Integer getExecutionYear();

	public Boolean isActive();

	public void setMaxIncrementNac(Integer integer);
	public void setMinIncrementNac(Integer integer);
	public void setWeigth(Integer integer);
	public void setBranch(IBranch branch);
	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setCurricularSemester(ICurricularSemester curricularSemester);
	public void setLabHours(Double double1);
	public void setPraticalHours(Double double1);
	public void setTheoPratHours(Double double1);
	public void setTheoreticalHours(Double double1);
	public void setCredits(Double double1);
	public void setEctsCredits(Double double1);
	public void setBeginDate(Calendar beginDate);
	public void setEndDate(Calendar endDate);
//	public void setExecutionYear(Integer integer);
}
