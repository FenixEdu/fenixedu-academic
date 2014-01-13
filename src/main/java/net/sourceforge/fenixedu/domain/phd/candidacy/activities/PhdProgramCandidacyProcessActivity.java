package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.log.PhdLog;

import org.fenixedu.bennu.core.domain.User;

public abstract class PhdProgramCandidacyProcessActivity extends Activity<PhdProgramCandidacyProcess> {

    @Override
    final public void checkPreConditions(final PhdProgramCandidacyProcess process, final User userView) {
        processPreConditions(process, userView);
        activityPreConditions(process, userView);
    }

    protected void processPreConditions(final PhdProgramCandidacyProcess process, final User userView) {
    }

    abstract protected void activityPreConditions(final PhdProgramCandidacyProcess process, final User userView);

    @Override
    protected void log(PhdProgramCandidacyProcess process, User userView, Object object) {
        PhdLog.logActivity(this, process, userView, object);
    }

}
