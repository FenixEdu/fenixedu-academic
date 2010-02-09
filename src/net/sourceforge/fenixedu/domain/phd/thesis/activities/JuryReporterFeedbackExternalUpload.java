/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdThesisReportFeedbackDocument;
import net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

public class JuryReporterFeedbackExternalUpload extends ExternalAccessPhdActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	// TODO Auto-generated method stub

	// chekc if already has documents ....

    }

    @Override
    protected PhdThesisProcess internalExecuteActivity(PhdThesisProcess process, IUserView userView, PhdExternalOperationBean bean) {

	final PhdProgramDocumentUploadBean documentBean = bean.getDocumentBean();
	if (documentBean.hasAnyInformation()) {
	    new PhdThesisReportFeedbackDocument(bean.getParticipant().getThesisJuryElement(process), documentBean.getType(),
		    documentBean.getRemarks(), documentBean.getFileContent(), documentBean.getFilename(), null);
	}

	return process;
    }
}