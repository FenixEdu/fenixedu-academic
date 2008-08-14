package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CandidateEnrolment;

/**
 * @author Luis Cruz
 * 
 */
public class InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidate extends InfoCandidateEnrolment {

    public void copyFromDomain(CandidateEnrolment candidateEnrolment) {
	super.copyFromDomain(candidateEnrolment);
	if (candidateEnrolment != null) {
	    setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(candidateEnrolment.getCurricularCourse()));
	    setInfoMasterDegreeCandidate(InfoMasterDegreeCandidate.newInfoFromDomain(candidateEnrolment
		    .getMasterDegreeCandidate()));
	}
    }

    public static InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidate newInfoFromDomain(
	    CandidateEnrolment candidateEnrolment) {
	InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidate infoCandidateEnrolment = null;
	if (candidateEnrolment != null) {
	    infoCandidateEnrolment = new InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidate();
	    infoCandidateEnrolment.copyFromDomain(candidateEnrolment);
	}
	return infoCandidateEnrolment;
    }

}