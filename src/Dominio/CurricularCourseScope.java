package Dominio;

import Util.ScopeType;


/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class CurricularCourseScope implements ICurricularCourseScope {

	private Integer internalID;
	private Integer curricularCourseKey;
	private Integer curricularSemesterKey;
	private Integer branchKey;
	
	private ICurricularCourse curricularCourse;
	private ICurricularSemester curricularSemester;
	private IBranch branch;
	ScopeType scopeType;
	
	public CurricularCourseScope() {
		setInternalID(null);

		setBranch(null);
		setCurricularCourse(null);
		setCurricularSemester(null);
		
		setBranchKey(null);
		setCurricularCourseKey(null);
		setCurricularSemesterKey(null);
		setScopeType(null);
	}

	public CurricularCourseScope(ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch) {
		this();
		setCurricularCourse(curricularCourse);
		setCurricularSemester(curricularSemester);
		setBranch(branch);
	}

	public CurricularCourseScope(ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch, ScopeType hasAlternativeSemester) {
		this();
		setCurricularCourse(curricularCourse);
		setCurricularSemester(curricularSemester);
		setBranch(branch);
		setScopeType(hasAlternativeSemester);
	}

	public boolean equals(Object obj) {

		boolean resultado = false;

		if (obj instanceof ICurricularCourseScope) {
			ICurricularCourseScope ccs = (ICurricularCourseScope) obj;
			
			resultado = getCurricularCourse().equals(ccs.getCurricularCourse()) &&
									getCurricularSemester().equals(ccs.getCurricularSemester()) &&
									getBranch().equals(ccs.getBranch());
		}

		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + this.internalID + "; ";
		result += "curricularCourse = " + this.curricularCourse + "; ";
		result += "curricularSemester = " + this.curricularSemester + "; ";
		result += "scopeType = " + this.scopeType + "; ";
		result += "branch = " + this.branch + "]\n";

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
	 * @return Integer
	 */
	public Integer getInternalID() {
		return internalID;
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

	/**
	 * Sets the internalID.
	 * @param internalID The internalID to set
	 */
	public void setInternalID(Integer internalID) {
		this.internalID = internalID;
	}

	/**
	 * @return ScopeType
	 */
	public ScopeType getScopeType() {
		return scopeType;
	}

	/**
	 * Sets the scopeType.
	 * @param scopeType The scopeType to set
	 */
	public void setScopeType(ScopeType hasAlternativeSemester) {
		this.scopeType = hasAlternativeSemester;
	}
}