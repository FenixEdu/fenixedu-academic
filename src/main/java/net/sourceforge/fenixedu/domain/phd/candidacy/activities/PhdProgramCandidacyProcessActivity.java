package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.log.PhdLog;

public abstract class PhdProgramCandidacyProcessActivity extends Activity<PhdProgramCandidacyProcess> {

    @Override
    final public void checkPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView) {
        processPreConditions(process, userView);
        activityPreConditions(process, userView);
    }

    protected void processPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView) {
    }

    abstract protected void activityPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView);

    @Override
    protected void log(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
        PhdLog.logActivity(this, process, userView, object);
    }

}
