package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class SuspendPhdProgramProcess extends PhdIndividualProgramProcessActivity {

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
        PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) object;

        DateTime stateDate = bean.getStateDate().toDateTimeAtStartOfDay();
        PhdProgramProcessState.createWithGivenStateDate(process, PhdIndividualProgramProcessState.SUSPENDED,
                userView.getPerson(), "", stateDate);

        process.cancelDebts(userView.getPerson());

        if (process.hasRegistration() && process.getRegistration().isActive()) {
            RegistrationStateCreator.createState(process.getRegistration(), userView.getPerson(), stateDate,
                    RegistrationStateType.INTERRUPTED);
        }

        return process;
    }
}