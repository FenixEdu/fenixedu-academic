package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;

public class RejectCandidacyProcess extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
        if (!process.isInState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {
        final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) object;
        process.createState(PhdProgramCandidacyProcessState.REJECTED, userView.getPerson(), bean.getRemarks());

        AlertService.alertAcademicOffice(process.getIndividualProgramProcess(), AcademicOperationType.VIEW_PHD_CANDIDACY_ALERTS,
                "message.phd.alert.candidacy.reject.subject", "message.phd.alert.candidacy.reject.body");

        return process;
    }

}
