package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;

public class DeleteAssistantGuiding extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, User userView) {

    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        return process.deleteAssistantGuiding((PhdParticipant) object);
    }
}