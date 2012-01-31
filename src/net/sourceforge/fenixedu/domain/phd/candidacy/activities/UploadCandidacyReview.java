package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class UploadCandidacyReview extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	if (process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
	    if ((isMasterDegreeAdministrativeOfficeEmployee(userView) || process.getIndividualProgramProcess()
		    .isCoordinatorForPhdProgram(userView.getPerson()))) {
		return;
	    }
	}

	if (process.isInState(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION)) {
	    if (isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		return;
	    }
	}

	throw new PreConditionNotValidException();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {

	    for (final PhdProgramDocumentUploadBean each : (List<PhdProgramDocumentUploadBean>) object) {
	    if (each.hasAnyInformation()) {
		process.addDocument(each, userView != null ? userView.getPerson() : null);
	    }
	}

	    return process;
    }

}
