/*
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */

package DataBeans;

import java.util.Calendar;

/**
 * @author tfc130
 */
public class InfoCurricularCourseScope extends InfoObject 
{
	private InfoCurricularCourse infoCurricularCourse;
	private InfoCurricularSemester infoCurricularSemester;
	private InfoBranch infoBranch;
	private Calendar beginDate;
	private Calendar endDate;

	private Integer maxIncrementNac;
	private Integer minIncrementNac;
	private Integer weigth;
	private Double theoreticalHours;
	private Double praticalHours;
	private Double theoPratHours;
	private Double labHours;
	private Double credits;
	private Double ectsCredits;

	public InfoCurricularCourseScope()
	{
	}

	public boolean equals(Object obj)
	{
		boolean resultado = false;
		if (obj instanceof InfoCurricularCourseScope)
		{
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) obj;
			resultado =
				(((getInfoBranch() == null && infoCurricularCourseScope.getInfoBranch() == null)
					|| (getInfoBranch() != null
						&& infoCurricularCourseScope.getInfoBranch() != null
						&& getInfoBranch().equals(infoCurricularCourseScope.getInfoBranch())))
					&& ((getInfoCurricularCourse() == null
						&& infoCurricularCourseScope.getInfoCurricularCourse() == null)
						|| (getInfoCurricularCourse() != null
							&& infoCurricularCourseScope.getInfoCurricularCourse() != null
							&& getInfoCurricularCourse().equals(
								infoCurricularCourseScope.getInfoCurricularCourse())))
					&& ((getInfoCurricularSemester() == null
						&& infoCurricularCourseScope.getInfoCurricularSemester() == null)
						|| (getInfoCurricularSemester() != null
							&& infoCurricularCourseScope.getInfoCurricularSemester() != null
							&& getInfoCurricularSemester().equals(
								infoCurricularCourseScope.getInfoCurricularSemester())))
					&& ((getEndDate() == null && infoCurricularCourseScope.getEndDate() == null)
						|| (getEndDate() != null
							&& infoCurricularCourseScope.getEndDate() != null
							&& getEndDate().equals(infoCurricularCourseScope.getEndDate()))));
		}
		return resultado;
	}

	public String toString()
	{
		String result = "[" + this.getClass().getName() + "; ";
		result += "CurricularCourse = " + this.infoCurricularCourse + "; ";
		result += "CurricularSemester = " + this.infoCurricularSemester + "; ";
		result += "Branch = " + this.infoBranch + "; ";
		result += "EndDate = " + this.endDate + "]\n";

		return result;
	}

	public Boolean isActive()
	{
		Boolean result = Boolean.FALSE;
		if (this.endDate == null)
		{
			result = Boolean.TRUE;
		}
		return result;
	}

	/**
	 * @return Returns the beginDate.
	 */
	public Calendar getBeginDate()
	{
		return beginDate;
	}

	/**
	 * @param beginDate The beginDate to set.
	 */
	public void setBeginDate(Calendar beginDate)
	{
		this.beginDate = beginDate;
	}

	/**
	 * @return Returns the endDate.
	 */
	public Calendar getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(Calendar endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return Returns the infoBranch.
	 */
	public InfoBranch getInfoBranch()
	{
		return infoBranch;
	}

	/**
	 * @param infoBranch The infoBranch to set.
	 */
	public void setInfoBranch(InfoBranch infoBranch)
	{
		this.infoBranch = infoBranch;
	}

	/**
	 * @return Returns the infoCurricularCourse.
	 */
	public InfoCurricularCourse getInfoCurricularCourse()
	{
		return infoCurricularCourse;
	}

	/**
	 * @param infoCurricularCourse The infoCurricularCourse to set.
	 */
	public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse)
	{
		this.infoCurricularCourse = infoCurricularCourse;
	}

	/**
	 * @return Returns the infoCurricularSemester.
	 */
	public InfoCurricularSemester getInfoCurricularSemester()
	{
		return infoCurricularSemester;
	}

	/**
	 * @param infoCurricularSemester The infoCurricularSemester to set.
	 */
	public void setInfoCurricularSemester(InfoCurricularSemester infoCurricularSemester)
	{
		this.infoCurricularSemester = infoCurricularSemester;
	}













	/**
	 * @deprecated
	 * @return Returns the credits.
	 */
	public Double getCredits()
	{
		return credits;
	}

	/**
	 * @deprecated
	 * @param credits The credits to set.
	 */
	public void setCredits(Double credits)
	{
		this.credits = credits;
	}

	/**
	 * @deprecated
	 * @return Returns the ectsCredits.
	 */
	public Double getEctsCredits()
	{
		return ectsCredits;
	}

	/**
	 * @deprecated
	 * @param ectsCredits The ectsCredits to set.
	 */
	public void setEctsCredits(Double ectsCredits)
	{
		this.ectsCredits = ectsCredits;
	}

	/**
	 * @deprecated
	 * @return Returns the labHours.
	 */
	public Double getLabHours()
	{
		return labHours;
	}

	/**
	 * @deprecated
	 * @param labHours The labHours to set.
	 */
	public void setLabHours(Double labHours)
	{
		this.labHours = labHours;
	}

	/**
	 * @deprecated
	 * @return Returns the maxIncrementNac.
	 */
	public Integer getMaxIncrementNac()
	{
		return maxIncrementNac;
	}

	/**
	 * @deprecated
	 * @param maxIncrementNac The maxIncrementNac to set.
	 */
	public void setMaxIncrementNac(Integer maxIncrementNac)
	{
		this.maxIncrementNac = maxIncrementNac;
	}

	/**
	 * @deprecated
	 * @return Returns the minIncrementNac.
	 */
	public Integer getMinIncrementNac()
	{
		return minIncrementNac;
	}

	/**
	 * @deprecated
	 * @param minIncrementNac The minIncrementNac to set.
	 */
	public void setMinIncrementNac(Integer minIncrementNac)
	{
		this.minIncrementNac = minIncrementNac;
	}

	/**
	 * @deprecated
	 * @return Returns the praticalHours.
	 */
	public Double getPraticalHours()
	{
		return praticalHours;
	}

	/**
	 * @deprecated
	 * @param praticalHours The praticalHours to set.
	 */
	public void setPraticalHours(Double praticalHours)
	{
		this.praticalHours = praticalHours;
	}

	/**
	 * @deprecated
	 * @return Returns the theoPratHours.
	 */
	public Double getTheoPratHours()
	{
		return theoPratHours;
	}

	/**
	 * @deprecated
	 * @param theoPratHours The theoPratHours to set.
	 */
	public void setTheoPratHours(Double theoPratHours)
	{
		this.theoPratHours = theoPratHours;
	}

	/**
	 * @deprecated
	 * @return Returns the theoreticalHours.
	 */
	public Double getTheoreticalHours()
	{
		return theoreticalHours;
	}

	/**
	 * @deprecated
	 * @param theoreticalHours The theoreticalHours to set.
	 */
	public void setTheoreticalHours(Double theoreticalHours)
	{
		this.theoreticalHours = theoreticalHours;
	}

	/**
	 * @deprecated
	 * @return Returns the weigth.
	 */
	public Integer getWeigth()
	{
		return weigth;
	}

	/**
	 * @deprecated
	 * @param weigth The weigth to set.
	 */
	public void setWeigth(Integer weigth)
	{
		this.weigth = weigth;
	}

}