/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdReporterReviewAlert;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class RequestJuryReviews extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

        if (!process.isJuryValidated()) {
            throw new PreConditionNotValidException();
        }

        if (process.hasState(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK)) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {

        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        if (bean.isToNotify()) {
            notifyJuryElements(process);
            sendAlertToJuryElement(process.getIndividualProgramProcess(), process.getPresidentJuryElement(),
                    "message.phd.request.jury.reviews.external.access.jury.president.body");
        }

        if (process.getActiveState() != PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK) {
            process.createState(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK, userView.getPerson(), "");
        }

        bean.setThesisProcess(process);
        if (!process.hasMeetingProcess()) {
            Process.createNewProcess(userView, PhdMeetingSchedulingProcess.class, bean);
        }

        return process;
    }

    private void notifyJuryElements(PhdThesisProcess process) {

        for (final ThesisJuryElement juryElement : process.getThesisJuryElements()) {

            if (juryElement.isDocumentValidated()) {
                continue;
            }

            if (!juryElement.isInternal()) {
                createExternalAccess(juryElement);
            }

            if (juryElement.getReporter().booleanValue()) {
                sendInitialAlertToReporter(process.getIndividualProgramProcess(), juryElement);
            } else {
                sendAlertToJuryElement(process.getIndividualProgramProcess(), juryElement,
                        "message.phd.request.jury.reviews.external.access.jury.body");
            }
        }

        for (final ThesisJuryElement juryElement : process.getThesisJuryElements()) {
            final PhdParticipant participant = juryElement.getParticipant();
            if (!juryElement.getReporter().booleanValue()) {
                continue;
            }

            new PhdReporterReviewAlert(process.getIndividualProgramProcess(), participant);
        }
    }

    private void sendInitialAlertToReporter(PhdIndividualProgramProcess process, ThesisJuryElement thesisJuryElement) {
        PhdParticipant participant = thesisJuryElement.getParticipant();

        if (!participant.isInternal()) {
            participant.ensureExternalAccess();
        }

        final AlertMessage subject =
                AlertMessage
                        .create(AlertMessage.get("message.phd.request.jury.reviews.external.access.subject", process
                                .getPhdProgram().getName())).isKey(false).withPrefix(false);

        final AlertMessage body =
                AlertMessage
                        .create(AlertMessage.get("message.phd.request.jury.reviews.external.access.jury.body", process
                                .getPerson().getName(), process.getProcessNumber())
                                + "\n\n"
                                + AlertMessage.get("message.phd.request.jury.reviews.reporter.body",
                                        getDaysLeftForReview(process.getThesisProcess()))
                                + "\n\n"
                                + getAccessInformation(process, participant,
                                        "message.phd.request.jury.reviews.coordinator.access",
                                        "message.phd.request.jury.reviews.teacher.access")).isKey(false).withPrefix(false);

        AlertService.alertParticipants(process, subject, body, participant);
    }

    private void sendAlertToJuryElement(PhdIndividualProgramProcess process, ThesisJuryElement thesisJuryElement,
            String bodyMessage) {
        PhdParticipant participant = thesisJuryElement.getParticipant();

        if (!participant.isInternal()) {
            createExternalAccess(thesisJuryElement);
            participant.ensureExternalAccess();
        }
        final AlertMessage subject =
                AlertMessage
                        .create(AlertMessage.get("message.phd.request.jury.reviews.external.access.subject", process
                                .getPhdProgram().getName())).isKey(false).withPrefix(false);

        final AlertMessage body =
                AlertMessage
                        .create(AlertMessage.get(bodyMessage, process.getPerson().getName(), process.getProcessNumber())
                                + "\n\n"
                                + getAccessInformation(process, participant,
                                        "message.phd.request.jury.reviews.coordinator.access",
                                        "message.phd.request.jury.reviews.teacher.access")
                                + "\n\n"
                                + AlertMessage.get("message.phd.request.jury.external.access.reviews.body",
                                        getDaysLeftForReview(process.getThesisProcess()))).isKey(false).withPrefix(false);

        AlertService.alertParticipants(process, subject, body, participant);
    }

    private void createExternalAccess(final ThesisJuryElement juryElement) {

        final PhdParticipant participant = juryElement.getParticipant();
        participant.addAccessType(PhdProcessAccessType.JURY_DOCUMENTS_DOWNLOAD);

        if (juryElement.getReporter().booleanValue()) {
            participant.addAccessType(PhdProcessAccessType.JURY_REPORTER_FEEDBACK_UPLOAD);
        }
    }

    private int getDaysLeftForReview(PhdThesisProcess process) {
        return Days.daysBetween(process.getWhenJuryValidated().plusDays(PhdReporterReviewAlert.getReporterReviewDeadlineDays()),
                new LocalDate()).getDays();
    }

}