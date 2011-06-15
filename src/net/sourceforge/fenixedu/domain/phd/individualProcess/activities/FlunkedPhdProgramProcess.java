package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.joda.time.DateTime;

public class FlunkedPhdProgramProcess extends PhdIndividualProgramProcessActivity {

    @Override
    protected void processPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
        // remove restrictions
    }

    @Override
    public void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
        if (!PhdIndividualProgramProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
    	throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
    	Object object) {
        process.createState(PhdIndividualProgramProcessState.FLUNKED, userView.getPerson());
        process.cancelDebts(userView.getPerson());

        if (process.hasRegistration() && process.getRegistration().isActive()) {
    	RegistrationStateCreator.createState(process.getRegistration(), userView.getPerson(), new DateTime(),
    		RegistrationStateType.CANCELED);
        }

        return process;
    }
}