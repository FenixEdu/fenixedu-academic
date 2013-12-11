package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class RemoveCandidacyDocument extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
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
