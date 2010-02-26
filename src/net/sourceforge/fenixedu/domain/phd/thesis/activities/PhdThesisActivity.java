package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.permissions.PhdPermissionType;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

abstract public class PhdThesisActivity extends Activity<PhdThesisProcess> {

    protected PhdPermissionType getThesisProcessPermission() {
	return PhdPermissionType.THESIS_PROCESS_MANAGEMENT;
    }

    @Override
    final public void checkPreConditions(final PhdThesisProcess process, final IUserView userView) {
	processPreConditions(process, userView);
	activityPreConditions(process, userView);
    }

    protected void processPreConditions(final PhdThesisProcess process, final IUserView userView) {
    }

    abstract protected void activityPreConditions(final PhdThesisProcess process, final IUserView userView);
}