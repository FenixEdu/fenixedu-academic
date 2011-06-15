package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class ValidatedByCandidate extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
    	Object object) {
        process.getCandidacyProcess().executeActivity(userView,
    	    PhdProgramCandidacyProcess.ValidatedByCandidate.class.getSimpleName(), null);
        return process;
    }
}