package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.JobBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

public class AddJobInformation extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	// no precondition to check
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {
	return process.addJobInformation(userView.getPerson(), (JobBean) object);
    }
}
