package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;

import org.joda.time.DateTime;

public class ConcludeIndividualProgramProcess extends PhdIndividualProgramProcessActivity {

    @Override
    protected void processPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
    }

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	if (!PhdIndividualProgramProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    throw new PreConditionNotValidException();
	}

	if (!hasPermissionFor(userView, PermissionType.MANAGE_PHD_PROCESS_STATES)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {

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
