package Dominio;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateEnrolment extends DomainObject implements ICandidateEnrolment {

	private IMasterDegreeCandidate masterDegreeCandidate;
	private ICurricularCourseScope curricularCourseScope;
	
	private Integer masterDegreeCandidateKey;
	private Integer curricularCourseScopeKey;
	
	public CandidateEnrolment() {
	}
		
	public CandidateEnrolment(IMasterDegreeCandidate masterDegreeCandidate, ICurricularCourseScope curricularCourseScope) {
		setMasterDegreeCandidate(masterDegreeCandidate);
		setCurricularCourseScope(curricularCourseScope);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICandidateEnrolment) {
			ICandidateEnrolment candidateEnrolment = (ICandidateEnrolment) obj;
			result = getMasterDegreeCandidate().equals(candidateEnrolment.getMasterDegreeCandidate())
						&& getCurricularCourseScope().equals(candidateEnrolment.getCurricularCourseScope());
		}
		return result;
	}

	public String toString() {
		String result = "[CANDIDATE_ENROLMENT";
		result += ", codInt=" + getIdInternal();
		result += ", masterDegreeCandidate=" + masterDegreeCandidate;
		result += ", curricularCourseScope=" + curricularCourseScope;
		result += "]";
		return result;
	}


	/**
	 * @return
	 */
	public ICurricularCourseScope getCurricularCourseScope() {
		return curricularCourseScope;
	}

	/**
	 * @return
	 */
	public Integer getCurricularCourseScopeKey() {
		return curricularCourseScopeKey;
	}

	/**
	 * @return
	 */
	public IMasterDegreeCandidate getMasterDegreeCandidate() {
		return masterDegreeCandidate;
	}

	/**
	 * @return
	 */
	public Integer getMasterDegreeCandidateKey() {
		return masterDegreeCandidateKey;
	}

	/**
	 * @param scope
	 */
	public void setCurricularCourseScope(ICurricularCourseScope scope) {
		curricularCourseScope = scope;
	}

	/**
	 * @param integer
	 */
	public void setCurricularCourseScopeKey(Integer integer) {
		curricularCourseScopeKey = integer;
	}

	/**
	 * @param candidate
	 */
	public void setMasterDegreeCandidate(IMasterDegreeCandidate candidate) {
		masterDegreeCandidate = candidate;
	}

	/**
	 * @param integer
	 */
	public void setMasterDegreeCandidateKey(Integer integer) {
		masterDegreeCandidateKey = integer;
	}

}
