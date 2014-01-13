/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdThesisReportFeedbackDocument;
import net.sourceforge.fenixedu.domain.phd.access.ExternalAccessPhdActivity;
import net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

import org.fenixedu.bennu.core.domain.User;

public class JuryReporterFeedbackExternalUpload extends ExternalAccessPhdActivity<PhdThesisProcess> {

    @Override
    public void checkPreConditions(PhdThesisProcess process, User userView) {
        // chekc if already has documents ....
    }

    @Override
    protected PhdThesisProcess internalExecuteActivity(PhdThesisProcess process, User userView, PhdExternalOperationBean bean) {

        final PhdProgramDocumentUploadBean documentBean = bean.getDocumentBean();
        if (documentBean.hasAnyInformation()) {
            new PhdThesisReportFeedbackDocument(bean.getParticipant().getThesisJuryElement(process), documentBean.getRemarks(),
                    documentBean.getFileContent(), documentBean.getFilename(), null);
        }

        return process;
    }

}