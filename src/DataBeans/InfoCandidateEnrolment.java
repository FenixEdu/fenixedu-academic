
package DataBeans;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoCandidateEnrolment  extends InfoObject{
	private InfoMasterDegreeCandidate infoMasterDegreeCandidate;
	private InfoCurricularCourseScope infoCurricularCourseScope;
	private InfoCurricularCourse infoCurricularCourse;
	

	public InfoCandidateEnrolment() {
	}

	public InfoCandidateEnrolment(InfoMasterDegreeCandidate infoMasterDegreeCandidate, InfoCurricularCourse infoCurricularCourse) {
		setInfoMasterDegreeCandidate(infoMasterDegreeCandidate);
		setInfoCurricularCourse(infoCurricularCourse);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoCandidateEnrolment) {
			InfoCandidateEnrolment infoCandidateEnrolment = (InfoCandidateEnrolment) obj;
			result = getInfoMasterDegreeCandidate().equals(infoCandidateEnrolment.getInfoMasterDegreeCandidate())
				&& getInfoCurricularCourse().equals(infoCandidateEnrolment.getInfoCurricularCourse());
		}
		return result;
	}

	public String toString() {
		String result = "[CANDIDATE_ENROLMENT";
		result += ", codInt=" + getIdInternal();
		result += ", infoMasterDegreeCandidate=" + infoMasterDegreeCandidate;
		result += ", infoCurricularCourse=" + infoCurricularCourse;
		result += "]";
		return result;
	}

	/**
	 * @return
	 * @deprecated
	 */
	public InfoCurricularCourseScope getInfoCurricularCourseScope() {
		return infoCurricularCourseScope;
	}

	/**
	 * @return
	 */
	public InfoMasterDegreeCandidate getInfoMasterDegreeCandidate() {
		return infoMasterDegreeCandidate;
	}

	/**
	 * @param course
	 * @deprecated
	 */
	public void setInfoCurricularCourseScope(InfoCurricularCourseScope courseScope) {
		infoCurricularCourseScope = courseScope;
	}

	/**
	 * @param candidate
	 */
	public void setInfoMasterDegreeCandidate(InfoMasterDegreeCandidate candidate) {
		infoMasterDegreeCandidate = candidate;
	}
	
	/**
	 * @return
	 */
	public InfoCurricularCourse getInfoCurricularCourse()
	{
		return infoCurricularCourse;
	}

	/**
	 * @param infoCurricularCourse
	 */
	public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse)
	{
		this.infoCurricularCourse = infoCurricularCourse;
	}

}