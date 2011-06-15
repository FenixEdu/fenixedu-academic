package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

public class AddQualification extends PhdIndividualProgramProcessActivity {

	@Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	// no precondition to check
    }

	@Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {
	return process.addQualification(userView != null ? userView.getPerson() : null, (QualificationBean) object);
    }
}
