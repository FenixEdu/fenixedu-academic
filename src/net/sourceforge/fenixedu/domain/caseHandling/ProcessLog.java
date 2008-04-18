package net.sourceforge.fenixedu.domain.caseHandling;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ProcessLog extends ProcessLog_Base {

    public ProcessLog(Process process, IUserView userView, Activity activity) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setProcess(process);
	setUserName(userView.getUtilizador());
	setActivity(activity.getClass().getName());
	setWhenDateTime(new DateTime());
    }

}
