package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

public class ValidatedByCandidate extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {
        process.getCandidacyProcess().executeActivity(userView,
                net.sourceforge.fenixedu.domain.phd.candidacy.activities.ValidatedByCandidate.class.getSimpleName(), null);
        return process;
    }
}