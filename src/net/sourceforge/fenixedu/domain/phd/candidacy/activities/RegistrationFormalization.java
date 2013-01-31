package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean;

public class RegistrationFormalization extends PhdProgramCandidacyProcessActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
		if (!process.isInState(PhdProgramCandidacyProcessState.RATIFIED_BY_SCIENTIFIC_COUNCIL)) {
			throw new PreConditionNotValidException();
		}
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
		return process.registrationFormalization((RegistrationFormalizationBean) object, userView.getPerson());
	}

}
