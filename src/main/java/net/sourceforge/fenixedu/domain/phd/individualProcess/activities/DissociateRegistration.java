package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

public class DissociateRegistration extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

        if (!process.hasRegistration()) {
            throw new PreConditionNotValidException();
        }

        if (!process.hasStudyPlan()) {
            throw new PreConditionNotValidException();
        }

        if (!process.getStudyPlan().isExempted()) {
            throw new PreConditionNotValidException();
        }

        if (!RegistrationStateType.CANCELED.equals(process.getRegistration().getActiveStateType())) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {
        process.setRegistration(null);

        return process;
    }

}
