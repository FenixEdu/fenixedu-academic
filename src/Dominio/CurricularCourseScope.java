package Dominio;

import java.util.Calendar;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class CurricularCourseScope extends DomainObject implements ICurricularCourseScope {

	private Integer curricularCourseKey;
	private Integer curricularSemesterKey;
	private Integer branchKey;

	private ICurricularCourse curricularCourse;
	private ICurricularSemester curricularSemester;
	private IBranch branch;
	private Calendar beginDate;
	private Calendar endDate;

	public CurricularCourseScope() {
		setIdInternal(null);
		setBranch(null);
		setCurricularCourse(null);
		setCurricularSemester(null);
		setBranchKey(null);
		setCurricularCourseKey(null);
		setCurricularSemesterKey(null);
	}

	/**
	 * @deprecated
	 */
	public CurricularCourseScope(ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch) {
		this();
		setCurricularCourse(curricularCourse);
		setCurricularSemester(curricularSemester);
		setBranch(branch);
	}

	public boolean equals(Object obj) {

		boolean resultado = false;

		if (obj instanceof ICurricularCourseScope) {
			ICurricularCourseScope ccs = (ICurricularCourseScope) obj;

			resultado =
				(((getBranch() == null && ccs.getBranch() == null)
					|| (getBranch() != null
						&& ccs.getBranch() != null
						&& getBranch().equals(ccs.getBranch())))
					&& ((getCurricularCourse() == null
						&& ccs.getCurricularCourse() == null)
						|| (getCurricularCourse() != null
							&& ccs.getCurricularCourse() != null
							&& getCurricularCourse().equals(
			ccs.getCurricularCourse())))
					&& ((getCurricularSemester() == null
						&& ccs.getCurricularSemester() == null)
						|| (getCurricularSemester() != null
							&& ccs.getCurricularSemester() != null
							&& getCurricularSemester().equals(
			ccs.getCurricularSemester())))
					&& ((getEndDate() == null && ccs.getEndDate() == null)
						|| (getEndDate() != null
							&& ccs.getEndDate() != null
							&& getEndDate().equals(ccs.getEndDate()))));
		}

		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + this.getIdInternal() + "; ";
		result += "curricularCourse = " + this.curricularCourse + "; ";
		result += "curricularSemester = " + this.curricularSemester + "; ";
		result += "branch = " + this.branch + "; ";
		result += "endDate = " + this.endDate + "]\n";

		return result;
	}

	public Boolean isActive() {
		Boolean result = Boolean.FALSE;
		if (this.endDate == null) {
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
	 * @return Returns the branch.
	 */
	public IBranch getBranch()
	{
		return branch;
	}

	/**
	 * @param branch The branch to set.
	 */
	public void setBranch(IBranch branch)
	{
		this.branch = branch;
	}

	/**
	 * @return Returns the branchKey.
	 */
	public Integer getBranchKey()
	{
		return branchKey;
	}

	/**
	 * @param branchKey The branchKey to set.
	 */
	public void setBranchKey(Integer branchKey)
	{
		this.branchKey = branchKey;
	}

	/**
	 * @return Returns the curricularCourse.
	 */
	public ICurricularCourse getCurricularCourse()
	{
		return curricularCourse;
	}

	/**
	 * @param curricularCourse The curricularCourse to set.
	 */
	public void setCurricularCourse(ICurricularCourse curricularCourse)
	{
		this.curricularCourse = curricularCourse;
	}

	/**
	 * @return Returns the curricularCourseKey.
	 */
	public Integer getCurricularCourseKey()
	{
		return curricularCourseKey;
	}

	/**
	 * @param curricularCourseKey The curricularCourseKey to set.
	 */
	public void setCurricularCourseKey(Integer curricularCourseKey)
	{
		this.curricularCourseKey = curricularCourseKey;
	}

	/**
	 * @return Returns the curricularSemester.
	 */
	public ICurricularSemester getCurricularSemester()
	{
		return curricularSemester;
	}

	/**
	 * @param curricularSemester The curricularSemester to set.
	 */
	public void setCurricularSemester(ICurricularSemester curricularSemester)
	{
		this.curricularSemester = curricularSemester;
	}

	/**
	 * @return Returns the curricularSemesterKey.
	 */
	public Integer getCurricularSemesterKey()
	{
		return curricularSemesterKey;
	}

	/**
	 * @param curricularSemesterKey The curricularSemesterKey to set.
	 */
	public void setCurricularSemesterKey(Integer curricularSemesterKey)
	{
		this.curricularSemesterKey = curricularSemesterKey;
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
}