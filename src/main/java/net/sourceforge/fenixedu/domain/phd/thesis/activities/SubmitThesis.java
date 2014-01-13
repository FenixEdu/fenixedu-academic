/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

import org.fenixedu.bennu.core.domain.User;

public class SubmitThesis extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.isJuryValidated()) {
            throw new PreConditionNotValidException();
        }

        if (!process.hasState(PhdThesisProcessStateType.THESIS_DISCUSSION_DATE_SCHECULED)) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        for (final PhdProgramDocumentUploadBean each : bean.getDocuments()) {
            if (each.hasAnyInformation()) {

                process.addDocument(each, userView.getPerson());

                PhdProgram phdProgram = process.getIndividualProgramProcess().getPhdProgram();
                if (!phdProgram.hasDegree()) {
                    continue;
                }

                if (!isThesisFinalDocument(each)) {
                    throw new DomainException("error.SubmitThesis.unexpected.document");
                }

                if (bean.isToNotify()) {
                    notifyAllElements(process, bean);
                }

            }
        }

        if (!process.hasState(PhdThesisProcessStateType.WAITING_FOR_THESIS_RATIFICATION)) {
            process.createState(PhdThesisProcessStateType.WAITING_FOR_THESIS_RATIFICATION, userView.getPerson(),
                    bean.getRemarks());
        }

        return process;

    }

    private boolean isThesisFinalDocument(final PhdProgramDocumentUploadBean each) {
        return each.getType() == PhdIndividualProgramDocumentType.FINAL_THESIS;
    }

    private void notifyAllElements(final PhdThesisProcess process, final PhdThesisProcessBean bean) {

        final AlertMessage subject = AlertMessage.create("message.phd.thesis.submit.subject");
        final AlertMessage body = AlertMessage.create("message.phd.thesis.submit.body");

        AlertService.alertResponsibleCoordinators(process.getIndividualProgramProcess(), subject, body);
        AlertService.alertGuiders(process.getIndividualProgramProcess(), subject, body);
        AlertService.alertStudent(process.getIndividualProgramProcess(), subject, body);

    }

}