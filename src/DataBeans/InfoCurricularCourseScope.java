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
}