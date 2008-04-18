package net.sourceforge.fenixedu.applicationTier.Servico.caseHandling;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ExecuteProcessActivity extends Service {

    public Process run(Process process, String activityId, Object object) {
	return process.executeActivity(AccessControl.getUserView(), activityId, object);
    }

    /*
     * public Process run(Integer processId, String activityId, Object object) {
     * return run(rootDomainObject.readProcessByOID(processId), activityId,
     * object); }
     */
}
