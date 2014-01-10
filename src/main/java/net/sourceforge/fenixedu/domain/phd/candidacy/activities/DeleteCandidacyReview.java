package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

import org.fenixedu.bennu.core.domain.User;

public class DeleteCandidacyReview extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
        if (process.isAllowedToManageProcess(userView)) {
            return;
        }

        if (process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
            if (process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
                return;
            }
        }

        throw new PreConditionNotValidException(
                "error.phd.candidacy.PhdProgramCandidacyProcess.DeleteCandidacyReview.cannot.delete.review.after.sending.for.ratification");

    }

    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {
        PhdProgramProcessDocument document = (PhdProgramProcessDocument) object;

        document.delete();

        return process;
    }

}
