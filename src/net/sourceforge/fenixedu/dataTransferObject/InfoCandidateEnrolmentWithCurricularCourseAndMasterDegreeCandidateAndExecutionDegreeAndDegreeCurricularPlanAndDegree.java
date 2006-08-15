package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

/**
 * @author Luis Cruz
 * 
 */
public class InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree extends InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidate {

    public void copyFromDomain(CandidateEnrolment candidateEnrolment) {
        super.copyFromDomain(candidateEnrolment);
        if (candidateEnrolment != null) {
            ExecutionDegree executionDegree = candidateEnrolment.getMasterDegreeCandidate().getExecutionDegree();
            getInfoMasterDegreeCandidate().setInfoExecutionDegree(InfoExecutionDegree.newInfoFromDomain(executionDegree));
        }
    }

    public static InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree newInfoFromDomain(CandidateEnrolment candidateEnrolment) {
        InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree infoCandidateEnrolment = null;
        if (candidateEnrolment != null) {
            infoCandidateEnrolment = new InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree();
            infoCandidateEnrolment.copyFromDomain(candidateEnrolment);
        }
        return infoCandidateEnrolment;
    }

}