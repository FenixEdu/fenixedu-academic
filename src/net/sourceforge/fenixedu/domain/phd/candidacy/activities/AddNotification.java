package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotification;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotificationBean;

public class AddNotification extends PhdProgramCandidacyProcessActivity {
	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
		if (!process.isAllowedToManageProcess(userView)) {
			throw new PreConditionNotValidException();
		}
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
		new PhdNotification((PhdNotificationBean) object);

		return process;
	}

}
