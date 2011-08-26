package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;

public class ActivatePhdProgramProcessInThesisDiscussionState extends PhdIndividualProgramProcessActivity {
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

	process.createState(PhdIndividualProgramProcessState.THESIS_DISCUSSION, userView.getPerson());

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