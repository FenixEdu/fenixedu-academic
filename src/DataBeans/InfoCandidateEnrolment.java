
package DataBeans;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoCandidateEnrolment  extends InfoObject{
	private InfoMasterDegreeCandidate infoMasterDegreeCandidate;
	private InfoCurricularCourseScope infoCurricularCourseScope;
	
	public InfoCandidateEnrolment() {
	}

	public InfoCandidateEnrolment(InfoMasterDegreeCandidate infoMasterDegreeCandidate, InfoCurricularCourseScope infoCurricularCourseScope) {
		setInfoMasterDegreeCandidate(infoMasterDegreeCandidate);
		setInfoCurricularCourseScope(infoCurricularCourseScope);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoCandidateEnrolment) {
			InfoCandidateEnrolment infoCandidateEnrolment = (InfoCandidateEnrolment) obj;
			result = getInfoMasterDegreeCandidate().equals(infoCandidateEnrolment.getInfoMasterDegreeCandidate())
				&& getInfoCurricularCourseScope().equals(infoCandidateEnrolment.getInfoCurricularCourseScope());
		}
		return result;
	}

	public String toString() {
		String result = "[CANDIDATE_ENROLMENT";
		result += ", codInt=" + getIdInternal();
		result += ", infoMasterDegreeCandidate=" + infoMasterDegreeCandidate;
		result += ", infoCurricularCourseScope=" + infoCurricularCourseScope;
		result += "]";
		return result;
	}

	/**
	 * @return
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

}