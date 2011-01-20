package net.sourceforge.fenixedu.domain.phd.migration.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.PhdThesisActivity;

public class SkipThesisJuryActivities extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	if (process.getActiveState() != PhdThesisProcessStateType.NEW) {
	    throw new PreConditionNotValidException();
	}

	if (!PhdThesisProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {

	process.createState(PhdThesisProcessStateType.WAITING_FOR_THESIS_MEETING_SCHEDULING, userView.getPerson(), "");

	return process;
    }

}
