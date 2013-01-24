package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.log.PhdLog;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;

public abstract class PhdIndividualProgramProcessActivity extends Activity<PhdIndividualProgramProcess> {

    @Override
    final public void checkPreConditions(final PhdIndividualProgramProcess process, final IUserView userView) {
	processPreConditions(process, userView);
	activityPreConditions(process, userView);
    }

    protected void processPreConditions(final PhdIndividualProgramProcess process, final IUserView userView) {
	if (process != null && !process.getActiveState().isActive()) {
	    throw new PreConditionNotValidException();
	}
    }

    protected void email(String email, String subject, String body) {
	final SystemSender sender = RootDomainObject.getInstance().getSystemSender();
	new Message(sender, sender.getConcreteReplyTos(), null, null, null, subject, body, Collections.singleton(email));
    }

    abstract protected void activityPreConditions(final PhdIndividualProgramProcess process, final IUserView userView);

    @Override
    protected void log(PhdIndividualProgramProcess process, IUserView userView, Object object) {
	PhdLog.logActivity(this, process, userView, object);
    }

}
