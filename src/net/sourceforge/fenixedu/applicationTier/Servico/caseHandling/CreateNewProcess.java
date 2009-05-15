package net.sourceforge.fenixedu.applicationTier.Servico.caseHandling;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;

public class CreateNewProcess extends FenixService {

    @Service
    public static Process run(String processName, Object object) {
	return Process.createNewProcess(AccessControl.getUserView(), processName, object);
    }

    @Service
    public static Process run(Class<? extends Process> processClass, Object object) {
	return Process.createNewProcess(AccessControl.getUserView(), processClass, object);
    }
}