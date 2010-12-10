package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.RemoteStudentCurricularPlan;

import org.joda.time.DateTime;

public class RemoteRegistration extends RemoteRegistration_Base {

    public RemoteRegistration() {
	super();
    }

    public Boolean isConcluded() {
	return toBoolean(readRemoteMethod("isConcluded"));
    }

    public DateTime getConclusionProcessCreationDateTime() {
	return toDateTime(readRemoteMethod("getConclusionProcessCreationDateTime"));
    }

    public RemoteStudentCurricularPlan getStudentCurricularPlanForCurrentExecutionYear() {
	return (RemoteStudentCurricularPlan) readRemoteDomainObjectByMethod("getStudentCurricularPlanForCurrentExecutionYear");

    }

}
