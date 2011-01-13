package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.RemoteExecutionYear;
import net.sourceforge.fenixedu.domain.RemoteStudentCurricularPlan;

public class RemoteRegistration extends RemoteRegistration_Base {

    public RemoteRegistration() {
	super();
    }

    public RemoteStudentCurricularPlan getStudentCurricularPlanForCurrentExecutionYear() {
	return (RemoteStudentCurricularPlan) readRemoteDomainObjectByMethod("getStudentCurricularPlanForCurrentExecutionYear");
    }

    public RemoteStudentCurricularPlan getLastStudentCurricularPlan() {
	return (RemoteStudentCurricularPlan) readRemoteDomainObjectByMethod("getLastStudentCurricularPlan");
    }

    public RemoteExecutionYear getConclusionYear() {
	return (RemoteExecutionYear) readRemoteDomainObjectByMethod("getConclusionYear");
    }

    public Boolean isRegistrationConclusionProcessed() {
	return toBoolean(readRemoteMethod("isRegistrationConclusionProcessed"));
    }

}
