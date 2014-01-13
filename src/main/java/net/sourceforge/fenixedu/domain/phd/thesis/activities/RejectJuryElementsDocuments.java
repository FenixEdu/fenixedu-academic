package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

import org.fenixedu.bennu.core.domain.User;

public class RejectJuryElementsDocuments extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.getActiveState().equals(PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION)) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

        if (process.hasJuryElementsDocument() && process.getJuryElementsDocument().getDocumentAccepted()) {
            return;
        }

        if (process.hasJuryPresidentDocument() && process.getJuryPresidentDocument().getDocumentAccepted()) {
            return;
        }

        throw new PreConditionNotValidException();
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        process.deleteLastState();

        process.rejectJuryElementsDocuments();

        if (bean.isToNotify()) {
            AlertService.alertCoordinators(process.getIndividualProgramProcess(),
                    "message.phd.alert.reject.jury.elements.documents.subject",
                    "message.phd.alert.reject.jury.elements.documents.body");
        }

        return process;

    }

}
