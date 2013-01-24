package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

import org.joda.time.LocalDate;

public class EditCandidacyDate extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	if (!process.isAllowedToManageProcess(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	return process.edit((LocalDate) object);
    }

}
