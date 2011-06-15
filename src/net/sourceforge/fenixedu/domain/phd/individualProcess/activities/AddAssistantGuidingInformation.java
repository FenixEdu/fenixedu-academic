package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;

public class AddAssistantGuidingInformation extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
        // no precondition to check
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
    	Object object) {
        return process.addAssistantGuiding((PhdParticipantBean) object);
    }
}