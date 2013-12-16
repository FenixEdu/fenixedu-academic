package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import java.util.List;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class UploadDocuments extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
        if (process.getActiveState() != PhdProgramCandidacyProcessState.PRE_CANDIDATE) {
            if (!process.isAllowedToManageProcess(userView)) {
                throw new PreConditionNotValidException();
            }
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {
        final List<PhdProgramDocumentUploadBean> documents = (List<PhdProgramDocumentUploadBean>) object;

        for (final PhdProgramDocumentUploadBean each : documents) {
            if (each.hasAnyInformation()) {
                process.addDocument(each, userView != null ? userView.getPerson() : null);
            }
        }

        return process;
    }

}
