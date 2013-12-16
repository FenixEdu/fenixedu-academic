package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class ValidatedByCandidate extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
    }

    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {
        process.setValidatedByCandidate(true);
        return process;
    }

}
