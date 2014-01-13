package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.phd.access.ExternalAccessPhdActivity;
import net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

import org.fenixedu.bennu.core.domain.User;

public class JuryReviewDocumentsDownload extends ExternalAccessPhdActivity<PhdThesisProcess> {

    @Override
    public void checkPreConditions(PhdThesisProcess process, User userView) {
        // TODO Auto-generated method stub
    }

    @Override
    protected PhdThesisProcess internalExecuteActivity(PhdThesisProcess process, User userView, PhdExternalOperationBean bean) {
        // for now is just for auditing purposes
        return process;
    }

}