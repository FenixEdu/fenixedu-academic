package net.sourceforge.fenixedu.domain;


/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CandidateEnrolment extends CandidateEnrolment_Base {

	public CandidateEnrolment() {
	}

	// public CandidateEnrolment(MasterDegreeCandidate masterDegreeCandidate,
	// CurricularCourseScope curricularCourseScope) {
	// setMasterDegreeCandidate(masterDegreeCandidate);
	// setCurricularCourseScope(curricularCourseScope);
	// }

	public CandidateEnrolment(MasterDegreeCandidate masterDegreeCandidate,
			CurricularCourse curricularCourse) {
		setMasterDegreeCandidate(masterDegreeCandidate);
		setCurricularCourse(curricularCourse);
	}

	public String toString() {
		String result = "[CANDIDATE_ENROLMENT";
		result += ", codInt=" + getIdInternal();
		result += ", masterDegreeCandidate=" + getMasterDegreeCandidate();
		result += ", curricularCourse=" + getCurricularCourse();
		result += "]";
		return result;
	}
}