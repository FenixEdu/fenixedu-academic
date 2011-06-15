package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

public class AddQualifications extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
	// no precondition to check
    }

	@Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {
	for (final QualificationBean bean : (List<QualificationBean>) object) {
	    process.addQualification(userView != null ? userView.getPerson() : null, bean);
	}
	return process;
    }
}
