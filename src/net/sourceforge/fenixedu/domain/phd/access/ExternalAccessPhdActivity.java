package net.sourceforge.fenixedu.domain.phd.access;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.log.PhdLog;

abstract public class ExternalAccessPhdActivity<T extends PhdProgramProcess> extends Activity<T> {

	@Override
	protected T executeActivity(T process, IUserView userView, Object object) {
		final PhdExternalOperationBean bean = (PhdExternalOperationBean) object;
		bean.getParticipant().checkAccessCredentials(bean.getEmail(), bean.getPassword());

		return internalExecuteActivity(process, userView, bean);
	}

	abstract protected T internalExecuteActivity(T process, IUserView userView, PhdExternalOperationBean bean);

	@Override
	protected void log(PhdProgramProcess process, IUserView userView, Object object) {
		PhdLog.logActivity(this, process, userView, object);
	}

}
