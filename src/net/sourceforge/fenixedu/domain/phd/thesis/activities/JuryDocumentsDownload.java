package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.phd.access.ExternalAccessPhdActivity;
import net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

public class JuryDocumentsDownload extends ExternalAccessPhdActivity<PhdThesisProcess> {

    @Override
    public void checkPreConditions(PhdThesisProcess process, IUserView userView) {
        // TODO Auto-generated method stub
    }

    @Override
    protected PhdThesisProcess internalExecuteActivity(PhdThesisProcess process, IUserView userView, PhdExternalOperationBean bean) {

        return process;
    }

}