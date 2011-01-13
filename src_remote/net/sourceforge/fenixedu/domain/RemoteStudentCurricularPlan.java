package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.studentCurriculum.RemoteCycleCurriculumGroup;

public class RemoteStudentCurricularPlan extends RemoteStudentCurricularPlan_Base {

    public RemoteStudentCurricularPlan() {
	super();
    }

    public Double getApprovedEctsCreditsForFirstCycle() {
	return toDouble(readRemoteMethod("getApprovedEctsCreditsForFirstCycle"));
    }

    public Double getApprovedEctsCreditsForSecondCycle() {
	return toDouble(readRemoteMethod("getApprovedEctsCreditsForSecondCycle"));
    }

    public Double getApprovedEctsCredits() {
	return toDouble(readRemoteMethod("getApprovedEctsCredits"));
    }

    public Boolean isConclusionProcessed() {
	return toBoolean(readRemoteMethod("isConclusionProcessed"));
    }

    public Boolean isFirstCycle() {
	return toBoolean(readRemoteMethod("isFirstCycle"));
    }

    public Boolean isSecondCycle() {
	return toBoolean(readRemoteMethod("isSecondCycle"));
    }

    public RemoteCycleCurriculumGroup getLastConcludedCycleCurriculumGroup() {
	return (RemoteCycleCurriculumGroup) readRemoteDomainObjectByMethod("getLastConcludedCycleCurriculumGroup");
    }

    public RemoteEnrolment getLatestDissertationEnrolment() {
	return (RemoteEnrolment) readRemoteDomainObjectByMethod("getLatestDissertationEnrolment");
    }

}
