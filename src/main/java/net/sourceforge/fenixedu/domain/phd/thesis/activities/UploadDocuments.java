package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import java.util.List;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

public class UploadDocuments extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final List<PhdProgramDocumentUploadBean> documents = (List<PhdProgramDocumentUploadBean>) object;

        for (final PhdProgramDocumentUploadBean each : documents) {
            if (each.hasAnyInformation()) {
                process.addDocument(each, userView != null ? userView.getPerson() : null);
            }
        }

        return process;
    }

}
