package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.CandidateEnrolment_Base;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateEnrolment extends CandidateEnrolment_Base {

	private IMasterDegreeCandidate masterDegreeCandidate;

	private ICurricularCourse curricularCourse;

	public CandidateEnrolment() {
	}

	// public CandidateEnrolment(IMasterDegreeCandidate masterDegreeCandidate,
	// ICurricularCourseScope curricularCourseScope) {
	// setMasterDegreeCandidate(masterDegreeCandidate);
	// setCurricularCourseScope(curricularCourseScope);
	// }

	public CandidateEnrolment(IMasterDegreeCandidate masterDegreeCandidate,
			ICurricularCourse curricularCourse) {
		setMasterDegreeCandidate(masterDegreeCandidate);
		setCurricularCourse(curricularCourse);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICandidateEnrolment) {
			ICandidateEnrolment candidateEnrolment = (ICandidateEnrolment) obj;
			result = getMasterDegreeCandidate().equals(
					candidateEnrolment.getMasterDegreeCandidate())
					&& getCurricularCourse().equals(
							candidateEnrolment.getCurricularCourse());
		}
		return result;
	}

	public String toString() {
		String result = "[CANDIDATE_ENROLMENT";
		result += ", codInt=" + getIdInternal();
		result += ", masterDegreeCandidate=" + masterDegreeCandidate;
		result += ", curricularCourse=" + curricularCourse;
		result += "]";
		return result;
	}

	/**
	 * @return
	 */
	public IMasterDegreeCandidate getMasterDegreeCandidate() {
		return masterDegreeCandidate;
	}

	/**
	 * @param candidate
	 */
	public void setMasterDegreeCandidate(IMasterDegreeCandidate candidate) {
		masterDegreeCandidate = candidate;
	}

	/**
	 * @return
	 */
	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	/**
	 * @param curricularCourse
	 */
	public void setCurricularCourse(ICurricularCourse curricularCourse) {
		this.curricularCourse = curricularCourse;
	}
}