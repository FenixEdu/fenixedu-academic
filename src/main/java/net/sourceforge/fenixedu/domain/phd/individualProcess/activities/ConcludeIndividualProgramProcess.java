package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;

import org.joda.time.DateTime;

public class ConcludeIndividualProgramProcess extends PhdIndividualProgramProcessActivity {

    @Override
    protected void processPreConditions(PhdIndividualProgramProcess process, User userView) {
    }

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (!process.isAllowedToManageProcessState(userView)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {

        if (!process.hasThesisProcess()) {
            throw new PhdDomainOperationException("error.PhdIndividualProgramProcess.thesis.process.inexistent");
        }

        if (!process.getThesisProcess().isConcluded()) {
            throw new PhdDomainOperationException("error.PhdIndividualProgramProcess.thesis.process.is.not.concluded");
        }

        PhdProgramProcessState.createWithGivenStateDate(process, PhdIndividualProgramProcessState.CONCLUDED,
                userView.getPerson(), "", new DateTime());

        return process;
    }

}
