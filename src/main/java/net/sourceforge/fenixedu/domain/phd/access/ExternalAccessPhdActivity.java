package net.sourceforge.fenixedu.domain.phd.access;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.log.PhdLog;

abstract public class ExternalAccessPhdActivity<T extends PhdProgramProcess> extends Activity<T> {

    @Override
    protected T executeActivity(T process, User userView, Object object) {
        final PhdExternalOperationBean bean = (PhdExternalOperationBean) object;
        bean.getParticipant().checkAccessCredentials(bean.getEmail(), bean.getPassword());

        return internalExecuteActivity(process, userView, bean);
    }

    abstract protected T internalExecuteActivity(T process, User userView, PhdExternalOperationBean bean);

    @Override
    protected void log(PhdProgramProcess process, User userView, Object object) {
        PhdLog.logActivity(this, process, userView, object);
    }

}
