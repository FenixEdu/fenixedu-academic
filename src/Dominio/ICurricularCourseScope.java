package Dominio;

import java.util.Calendar;



/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public interface ICurricularCourseScope extends IDomainObject
{
	/**
	 * @deprecated
	 */
	public Integer getMaxIncrementNac();
	/**
	 * @deprecated
	 */
	public Integer getMinIncrementNac();
	/**
	 * @deprecated
	 */
	public Integer getWeigth();
	/**
	 * @deprecated
	 */
	public Double getLabHours();
	/**
	 * @deprecated
	 */
	public Double getPraticalHours();
	/**
	 * @deprecated
	 */
	public Double getTheoPratHours();
	/**
	 * @deprecated
	 */
	public Double getTheoreticalHours();
	/**
	 * @deprecated
	 */
	public Double getCredits();
	/**
	 * @deprecated
	 */
	public Double getEctsCredits();

	/**
	 * @deprecated
	 */
	public void setMaxIncrementNac(Integer integer);
	/**
	 * @deprecated
	 */
	public void setMinIncrementNac(Integer integer);
	/**
	 * @deprecated
	 */
	public void setWeigth(Integer integer);
	/**
	 * @deprecated
	 */
	public void setLabHours(Double double1);
	/**
	 * @deprecated
	 */
	public void setPraticalHours(Double double1);
	/**
	 * @deprecated
	 */
	public void setTheoPratHours(Double double1);
	/**
	 * @deprecated
	 */
	public void setTheoreticalHours(Double double1);
	/**
	 * @deprecated
	 */
	public void setCredits(Double double1);
	/**
	 * @deprecated
	 */
	public void setEctsCredits(Double double1);




	public IBranch getBranch();
	public ICurricularCourse getCurricularCourse();
	public ICurricularSemester getCurricularSemester();
	public Calendar getBeginDate();
	public Calendar getEndDate();

	public void setBranch(IBranch branch);
	public void setCurricularCourse(ICurricularCourse curricularCourse);
	public void setCurricularSemester(ICurricularSemester curricularSemester);
	public void setBeginDate(Calendar beginDate);
	public void setEndDate(Calendar endDate);

	public Boolean isActive();
}