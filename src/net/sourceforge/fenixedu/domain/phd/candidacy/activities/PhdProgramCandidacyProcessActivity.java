package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.log.PhdLog;
import net.sourceforge.fenixedu.domain.phd.permissions.PhdPermissionType;

public abstract class PhdProgramCandidacyProcessActivity extends Activity<PhdProgramCandidacyProcess> {

    protected PhdPermissionType getCandidacyProcessManagementPermission() {
	return PhdPermissionType.CANDIDACY_PROCESS_MANAGEMENT;
    }

    @Override
    final public void checkPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView) {
	processPreConditions(process, userView);
	activityPreConditions(process, userView);
    }

    protected void processPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView) {
    }

    abstract protected void activityPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView);

    static public boolean isMasterDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView != null && userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isMasterDegree();
    }

    @Override
    protected void log(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	PhdLog.logActivity(this, process, userView, object);
    }

}
