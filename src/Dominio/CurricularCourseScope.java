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

	private Integer maxIncrementNac;
	private Integer minIncrementNac;
	private Integer weigth;
	//	private Integer executionYear;

	private Double theoreticalHours;
	private Double praticalHours;
	private Double theoPratHours;
	private Double labHours;
	private Double credits;
	private Double ectsCredits;

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

	/**
	 * @return Branch
	 */
	public IBranch getBranch() {
		return branch;
	}

	/**
	 * @return Integer
	 */
	public Integer getBranchKey() {
		return branchKey;
	}

	/**
	 * @return CurricularCourse
	 */
	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	/**
	 * @return Integer
	 */
	public Integer getCurricularCourseKey() {
		return curricularCourseKey;
	}

	/**
	 * @return CurricularSemester
	 */
	public ICurricularSemester getCurricularSemester() {
		return curricularSemester;
	}

	/**
	 * @return Integer
	 */
	public Integer getCurricularSemesterKey() {
		return curricularSemesterKey;
	}

	/**
	 * Sets the branch.
	 * @param branch The branch to set
	 */
	public void setBranch(IBranch branch) {
		this.branch = branch;
	}

	/**
	 * Sets the branchKey.
	 * @param branchKey The branchKey to set
	 */
	public void setBranchKey(Integer branchKey) {
		this.branchKey = branchKey;
	}

	/**
	 * Sets the curricularCourse.
	 * @param curricularCourse The curricularCourse to set
	 */
	public void setCurricularCourse(ICurricularCourse curricularCourse) {
		this.curricularCourse = curricularCourse;
	}

	/**
	 * Sets the curricularCourseKey.
	 * @param curricularCourseKey The curricularCourseKey to set
	 */
	public void setCurricularCourseKey(Integer curricularCourseKey) {
		this.curricularCourseKey = curricularCourseKey;
	}

	/**
	 * Sets the curricularSemester.
	 * @param curricularSemester The curricularSemester to set
	 */
	public void setCurricularSemester(ICurricularSemester curricularSemester) {
		this.curricularSemester = curricularSemester;
	}

	/**
	 * Sets the curricularSemesterKey.
	 * @param curricularSemesterKey The curricularSemesterKey to set
	 */
	public void setCurricularSemesterKey(Integer curricularSemesterKey) {
		this.curricularSemesterKey = curricularSemesterKey;
	}

	public Integer getMaxIncrementNac() {
		return maxIncrementNac;
	}

	public Integer getMinIncrementNac() {
		return minIncrementNac;
	}

	public Integer getWeigth() {
		return weigth;
	}

	public void setMaxIncrementNac(Integer integer) {
		maxIncrementNac = integer;
	}

	public void setMinIncrementNac(Integer integer) {
		minIncrementNac = integer;
	}

	public void setWeigth(Integer integer) {
		weigth = integer;
	}

	public Double getLabHours() {
		return labHours;
	}

	public Double getPraticalHours() {
		return praticalHours;
	}

	public Double getTheoPratHours() {
		return theoPratHours;
	}

	public Double getTheoreticalHours() {
		return theoreticalHours;
	}

	public void setLabHours(Double double1) {
		labHours = double1;
	}

	public void setPraticalHours(Double double1) {
		praticalHours = double1;
	}

	public void setTheoPratHours(Double double1) {
		theoPratHours = double1;
	}

	public void setTheoreticalHours(Double double1) {
		theoreticalHours = double1;
	}

	public Double getCredits() {
		return credits;
	}

	public void setCredits(Double double1) {
		credits = double1;
	}

	//	public Integer getExecutionYear() {
	//		return executionYear;
	//	}
	//
	//	public void setExecutionYear(Integer integer) {
	//		executionYear = integer;
	//	}
	/**
	 * @return
	 */
	public Calendar getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate
	 */
	public void setBeginDate(Calendar beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return
	 */
	public Double getEctsCredits() {
		return ectsCredits;
	}

	/**
	 * @param ectsCredits
	 */
	public void setEctsCredits(Double ectsCredits) {
		this.ectsCredits = ectsCredits;
	}

	public Boolean isActive() {
		Boolean result = Boolean.FALSE;
		if (this.endDate == null) {
			result = Boolean.TRUE;
		}
		return result;
	}

}