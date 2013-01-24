package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

public class RemoveLastState extends PhdThesisActivity {

    @Override
    protected void processPreConditions(PhdThesisProcess process, IUserView userView) {
    }

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	if (!process.isAllowedToManageProcess(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	process.removeLastState();

	return process;
    }

}
