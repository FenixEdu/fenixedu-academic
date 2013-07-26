package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;

public class ActivatePhdProgramProcessInThesisDiscussionState extends PhdIndividualProgramProcessActivity {
    @Override
    protected void processPreConditions(PhdIndividualProgramProcess process, User userView) {
        // remove restrictions
    }

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (!process.isAllowedToManageProcessState(userView)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {

        PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) object;

        PhdProgramProcessState.createWithGivenStateDate(process, PhdIndividualProgramProcessState.THESIS_DISCUSSION,
                userView.getPerson(), "", bean.getStateDate().toDateTimeAtStartOfDay());

        /*
         * If the program is associated to a registration we check if it is
         * concluded
         */

        if (!process.hasRegistration()) {
            return process;
        }

        if (!process.getRegistration().isConcluded() && !process.getRegistration().isSchoolPartConcluded()) {
            throw new DomainException(
                    "error.PhdIndividualProgramProcess.set.thesis.discussion.state.registration.is.not.concluded");
        }

        return process;
    }
}