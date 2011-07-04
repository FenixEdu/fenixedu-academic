package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.studentCurriculum.RemoteCycleCurriculumGroup;

public class RemoteStudentCurricularPlan extends RemoteStudentCurricularPlan_Base {

    public RemoteStudentCurricularPlan() {
	super();
    }

    public Double getApprovedEctsCreditsForFirstCycle() {
	return toDouble(readRemoteMethod("getApprovedEctsCreditsForFirstCycle", null));
    }

    public Double getApprovedEctsCreditsForSecondCycle() {
	return toDouble(readRemoteMethod("getApprovedEctsCreditsForSecondCycle", null));
    }

    public Double getApprovedEctsCredits() {
	return toDouble(readRemoteMethod("getApprovedEctsCredits", null));
    }

    public Boolean isConclusionProcessed() {
	return toBoolean(readRemoteMethod("isConclusionProcessed", null));
    }

    public Boolean isFirstCycle() {
	return toBoolean(readRemoteMethod("isFirstCycle", null));
    }

    public Boolean isSecondCycle() {
	return toBoolean(readRemoteMethod("isSecondCycle", null));
    }

    public RemoteCycleCurriculumGroup getLastConcludedCycleCurriculumGroup() {
	return (RemoteCycleCurriculumGroup) readRemoteDomainObjectByMethod("getLastConcludedCycleCurriculumGroup", null);
    }

    public RemoteEnrolment getLatestDissertationEnrolment() {
	return (RemoteEnrolment) readRemoteDomainObjectByMethod("getLatestDissertationEnrolment", null);
    }

}
