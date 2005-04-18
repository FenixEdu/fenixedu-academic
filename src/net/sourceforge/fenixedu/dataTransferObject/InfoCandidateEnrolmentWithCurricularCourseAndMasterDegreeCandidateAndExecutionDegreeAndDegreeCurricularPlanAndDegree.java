package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ICandidateEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionDegree;

/**
 * @author Luis Cruz
 * 
 */
public class InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree extends InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidate {

    public void copyFromDomain(ICandidateEnrolment candidateEnrolment) {
        super.copyFromDomain(candidateEnrolment);
        if (candidateEnrolment != null) {
            IExecutionDegree executionDegree = candidateEnrolment.getMasterDegreeCandidate().getExecutionDegree();
            getInfoMasterDegreeCandidate().setInfoExecutionDegree(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan.newInfoFromDomain(executionDegree));
        }
    }

    public static InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree newInfoFromDomain(ICandidateEnrolment candidateEnrolment) {
        InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree infoCandidateEnrolment = null;
        if (candidateEnrolment != null) {
            infoCandidateEnrolment = new InfoCandidateEnrolmentWithCurricularCourseAndMasterDegreeCandidateAndExecutionDegreeAndDegreeCurricularPlanAndDegree();
            infoCandidateEnrolment.copyFromDomain(candidateEnrolment);
        }
        return infoCandidateEnrolment;
    }

}