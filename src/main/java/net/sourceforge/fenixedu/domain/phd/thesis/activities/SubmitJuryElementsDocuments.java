/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

public class SubmitJuryElementsDocuments extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (process.isAllowedToManageProcess(userView)) {
            return;
        }

        if (process.isJuryValidated()) {
            throw new PreConditionNotValidException();
        }

        if (!process.hasState(PhdThesisProcessStateType.WAITING_FOR_JURY_CONSTITUTION)) {
            throw new PreConditionNotValidException();
        }

        if (userView.getPerson() != null
                && process.getIndividualProgramProcess().isCoordinatorForPhdProgram(userView.getPerson())) {
            return;
        }

        throw new PreConditionNotValidException();
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
        boolean anyDocumentSubmitted = false;

        process.setWhenJuryDesignated(bean.getWhenJuryDesignated());

        for (final PhdProgramDocumentUploadBean each : bean.getDocuments()) {
            if (each.hasAnyInformation()) {

                process.addDocument(each, userView.getPerson());

                if (bean.getGenerateAlert()) {
                    alertIfNecessary(bean, process, each, userView.getPerson());
                }

                anyDocumentSubmitted = true;
            }
        }

        if (anyDocumentSubmitted) {
            if (!process.hasState(PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION)) {
                process.createState(PhdThesisProcessStateType.JURY_WAITING_FOR_VALIDATION, userView.getPerson(),
                        bean.getRemarks());
            }
        }

        return process;
    }

    private void alertIfNecessary(PhdThesisProcessBean bean, PhdThesisProcess process, PhdProgramDocumentUploadBean each,
            Person person) {

        switch (each.getType()) {
        case JURY_PRESIDENT_ELEMENT:

            if (bean.getGenerateAlert()) {
                AlertService.alertCoordinators(process.getIndividualProgramProcess(),
                        "message.phd.alert.request.jury.president.subject", "message.phd.alert.request.jury.president.body");
            }
            break;

        case JURY_ELEMENTS:
            if (process.getIndividualProgramProcess().isCoordinatorForPhdProgram(person)) {
                AlertService.alertAcademicOffice(process.getIndividualProgramProcess(),
                        AcademicOperationType.VIEW_PHD_THESIS_ALERTS, "message.phd.alert.jury.elements.submitted.subject",
                        "message.phd.alert.jury.elements.submitted.body");
            }
            break;
        }
    }
}