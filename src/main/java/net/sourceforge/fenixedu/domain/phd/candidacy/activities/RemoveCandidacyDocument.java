package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

import org.fenixedu.bennu.core.domain.User;

public class RemoveCandidacyDocument extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
        User user = userView != null ? userView : process.getPerson().getUser();
        if (!process.isAllowedToManageProcess(user)) {
            return;
        }

        if (process.isPublicCandidacy() && process.getPublicPhdCandidacyPeriod().isOpen()) {
            return;
        }

        throw new PreConditionNotValidException();
    }

    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {
        PhdProgramProcessDocument phdDocument = (PhdProgramProcessDocument) object;

        phdDocument.removeFromProcess();

        return process;
    }

}
