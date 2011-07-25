package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.RemoteExecutionYear;
import net.sourceforge.fenixedu.domain.RemoteStudentCurricularPlan;
import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;

public class RemoteRegistration extends RemoteRegistration_Base {

    public RemoteRegistration() {
	super();
    }

    public RemoteStudentCurricularPlan getStudentCurricularPlanForCurrentExecutionYear() {
	return (RemoteStudentCurricularPlan) readRemoteDomainObjectByMethod("getStudentCurricularPlanForCurrentExecutionYear",
		null);
    }

    public RemoteStudentCurricularPlan getLastStudentCurricularPlan() {
	return (RemoteStudentCurricularPlan) readRemoteDomainObjectByMethod("getLastStudentCurricularPlan", null);
    }

    public RemoteExecutionYear getConclusionYear() {
	return (RemoteExecutionYear) readRemoteDomainObjectByMethod("getConclusionYear", null);
    }

    public Boolean isRegistrationConclusionProcessed() {
	return toBoolean(readRemoteMethod("isRegistrationConclusionProcessed", null));
    }

    public static String readAllStudentInfo(RemoteHost host) {
	return host.readRemoteStaticMethod("net.sourceforge.fenixedu.domain.student.Registration", "readAllStudentInfo", null);
    }

    public Integer getFinalAverageOfLastConcludedCycle() {
	return toInteger(readRemoteMethod("getFinalAverageOfLastConcludedCycle", null));
    }
}
