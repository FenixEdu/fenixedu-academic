package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;

import org.joda.time.DateTime;

public class ActivatePhdProgramProcessInWorkDevelopmentState extends PhdIndividualProgramProcessActivity {
    @Override
    protected void processPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	// remove restrictions
    }

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	if (!PhdIndividualProgramProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {

	process.createState(PhdIndividualProgramProcessState.WORK_DEVELOPMENT, userView.getPerson());

	/*
	 * If it is associated to a registration we check that is not active and
	 * try to reactivate it setting the last active state of the
	 * registration
	 */

	if (!process.hasRegistration()) {
	    return process;
	}

	/*
	 * The registration is concluded so we skip
	 */
	if (process.getRegistration().isConcluded()) {
	    return process;
	}

	if (process.getRegistration().isActive()) {
	    throw new DomainException("error.PhdIndividualProgramProcess.set.work.development.state.registration.is.active");
	}

	RegistrationState registrationLastActiveState = process.getRegistration().getLastActiveState();

	if (!registrationLastActiveState.isActive()) {
	    throw new DomainException(
		    "error.PhdIndividualProgramProcess.set.work.development.state.registration.last.state.is.not.active");
	}

	RegistrationStateCreator.createState(process.getRegistration(), userView.getPerson(), new DateTime(),
		registrationLastActiveState.getStateType());

	return process;
    }
}