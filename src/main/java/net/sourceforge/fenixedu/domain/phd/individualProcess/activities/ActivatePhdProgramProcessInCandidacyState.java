package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;

public class ActivatePhdProgramProcessInCandidacyState extends PhdIndividualProgramProcessActivity {

    @Override
    protected void processPreConditions(PhdIndividualProgramProcess process, User userView) {
        // remove restrictions
    }

    @Override
    public void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (!process.isAllowedToManageProcessState(userView)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        final PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) object;

        /*
         * 1 - Check if there's no registration 2 - Check if last active state
         * was candidacy
         */
        if (process.hasRegistration()) {
            throw new DomainException("error.PhdIndividualProgramProcess.set.candidacy.state.has.registration");
        }

        if (!process.getLastActiveState().getType().equals(PhdIndividualProgramProcessState.CANDIDACY)) {
            throw new DomainException(
                    "error.PhdIndividualProgramProcess.set.candidacy.state.previous.active.state.is.not.candidacy");
        }

        PhdProgramProcessState.createWithGivenStateDate(process, process.getLastActiveState().getType(), userView.getPerson(),
                "", bean.getStateDate().toDateTimeAtStartOfDay());

        return process;
    }
}