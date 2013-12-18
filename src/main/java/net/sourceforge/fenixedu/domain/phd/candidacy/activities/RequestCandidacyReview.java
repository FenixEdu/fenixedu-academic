package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import java.util.Arrays;
import java.util.List;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.Instalation;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;

public class RequestCandidacyReview extends PhdProgramCandidacyProcessActivity {

    static final private List<PhdProgramCandidacyProcessState> PREVIOUS_STATE = Arrays.asList(

    PhdProgramCandidacyProcessState.PRE_CANDIDATE,

    PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION,

    PhdProgramCandidacyProcessState.STAND_BY_WITH_COMPLETE_INFORMATION,

    PhdProgramCandidacyProcessState.REJECTED,

    PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION);

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
        if (PREVIOUS_STATE.contains(process.getActiveState())) {
            return;
        }
        throw new PreConditionNotValidException();
    }

    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {

        final PhdIndividualProgramProcess mainProcess = process.getIndividualProgramProcess();
        if (!mainProcess.hasPhdProgram()) {
            throw new DomainException("error.phd.candidacy.PhdProgramCandidacyProcess.RequestCandidacyReview.invalid.phd.program");
        }

        final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) object;
        process.createState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION, userView.getPerson(),
                bean.getRemarks());

        if (bean.getGenerateAlert()) {
            AlertService.alertCoordinators(mainProcess, subject(), body(mainProcess));
        }

        return process;
    }

    private AlertMessage subject() {
        return AlertMessage.create("message.phd.alert.candidacy.review.subject");
    }

    private AlertMessage body(final PhdIndividualProgramProcess process) {
        return AlertMessage.create("message.phd.alert.candidacy.review.body").args(process.getProcessNumber(),
                process.getPerson().getName(), Instalation.getInstance().getInstituitionalEmailAddress("suporte"));
    }

}
