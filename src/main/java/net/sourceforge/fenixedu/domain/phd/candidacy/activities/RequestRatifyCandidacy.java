package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;

public class RequestRatifyCandidacy extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
        if (!process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {
        final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) object;

        if (!process.getIndividualProgramProcess().getPhdConfigurationIndividualProgramProcess().isMigratedProcess()
                && process.getCandidacyReviewDocuments().isEmpty()) {
            throw new DomainException(
                    "error.phd.candidacy.PhdProgramCandidacyProcess.RequestRatifyCandidacy.candidacy.review.document.is.required");
        }

        process.createState(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION, userView.getPerson(),
                bean.getRemarks());

        if (bean.getGenerateAlert()) {
            AlertService.alertAcademicOffice(process.getIndividualProgramProcess(),
                    AcademicOperationType.VIEW_PHD_CANDIDACY_ALERTS, "message.phd.alert.candidacy.request.ratify.subject",
                    "message.phd.alert.candidacy.request.ratify.body");
        }

        return process;
    }

}
