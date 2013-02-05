package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.alert.PhdReporterReviewAlert;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class RemindJuryReviewToReporters extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
        if (process.getActiveState() != PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK) {
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
            remindReporters(process);
        }

        return process;
    }

    private void remindReporters(PhdThesisProcess process) {
        for (final ThesisJuryElement juryElement : process.getThesisJuryElements()) {

            if (!juryElement.getReporter().booleanValue()) {
                continue;
            }

            if (juryElement.isDocumentValidated()) {
                continue;
            }

            final PhdParticipant participant = juryElement.getParticipant();

            sendReminderToReporter(process.getIndividualProgramProcess(), participant);
        }
    }

    private void sendReminderToReporter(PhdIndividualProgramProcess process, PhdParticipant participant) {
        final AlertMessage subject =
                AlertMessage
                        .create(AlertMessage.get("message.phd.remind.jury.reviews.subject", process.getPhdProgram().getName()))
                        .isKey(false).withPrefix(false);

        String partialBody = null;

        if (!hasExceededLimitForReview(process.getThesisProcess())) {
            partialBody =
                    AlertMessage.get("message.phd.remind.jury.reviews.body", process.getPerson().getName(),
                            process.getProcessNumber(), daysLeftUntilDeadline(process.getThesisProcess()));
        } else {
            partialBody =
                    AlertMessage.get("message.phd.remind.jury.reviews.body.late", process.getPerson().getName(),
                            process.getProcessNumber());
        }

        final AlertMessage body =
                AlertMessage
                        .create(partialBody
                                + "\n\n"
                                + getAccessInformation(process, participant,
                                        "message.phd.request.jury.reviews.coordinator.access",
                                        "message.phd.request.jury.reviews.teacher.access")).isKey(false).withPrefix(false);

        AlertService.alertParticipants(process, subject, body, participant);
    }

    private boolean hasExceededLimitForReview(PhdThesisProcess process) {
        return !(new LocalDate().isBefore(process.getWhenJuryValidated().plusDays(
                PhdReporterReviewAlert.getReporterReviewDeadlineDays())));
    }

    private int daysLeftUntilDeadline(PhdThesisProcess process) {
        return Days.daysBetween(new LocalDate(),
                process.getWhenJuryValidated().plusDays(PhdReporterReviewAlert.getReporterReviewDeadlineDays())).getDays();
    }

}
