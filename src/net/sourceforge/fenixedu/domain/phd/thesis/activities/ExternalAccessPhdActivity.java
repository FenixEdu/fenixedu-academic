/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

abstract class ExternalAccessPhdActivity extends PhdThesisActivity {

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	final PhdExternalOperationBean bean = (PhdExternalOperationBean) object;
	bean.getParticipant().checkAccessCredentials(bean.getEmail(), bean.getPassword());

	return internalExecuteActivity(process, userView, bean);
    }

    abstract protected PhdThesisProcess internalExecuteActivity(PhdThesisProcess process, IUserView userView,
	    PhdExternalOperationBean bean);

}