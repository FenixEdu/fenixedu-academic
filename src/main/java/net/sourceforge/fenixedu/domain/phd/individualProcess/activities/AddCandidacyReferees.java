package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.fenixedu.bennu.core.domain.User;

public class AddCandidacyReferees extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        // nothing to be done for now
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        process.getCandidacyProcess().executeActivity(userView,
                net.sourceforge.fenixedu.domain.phd.candidacy.activities.AddCandidacyReferees.class.getSimpleName(), object);
        return process;
    }
}