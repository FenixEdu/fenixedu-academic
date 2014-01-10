package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

import org.fenixedu.bennu.core.domain.User;

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
