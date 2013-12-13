package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ReplaceDocument extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        PhdProgramDocumentUploadBean documentBean = (PhdProgramDocumentUploadBean) object;
        PhdProgramProcessDocument document = process.getLatestDocumentVersionFor(documentBean.getType());

        document.replaceDocument(documentBean.getType(), documentBean.getRemarks(), documentBean.getFileContent(),
                documentBean.getFilename(), AccessControl.getPerson());

        return process;
    }
}
