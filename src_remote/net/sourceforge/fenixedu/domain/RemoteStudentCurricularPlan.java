package net.sourceforge.fenixedu.domain;

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
}
