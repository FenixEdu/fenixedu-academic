/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.phd.thesis.activities;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.alert.AlertService.AlertMessage;
import org.fenixedu.academic.domain.phd.alert.PhdReporterReviewAlert;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType;
import org.fenixedu.academic.domain.phd.thesis.ThesisJuryElement;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class RemindJuryReviewToReporters extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (process.getActiveState() != PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        if (bean.isToNotify()) {
            remindReporters(process);
        }

        return process;
    }

    private void remindReporters(PhdThesisProcess process) {
        for (final ThesisJuryElement juryElement : process.getThesisJuryElementsSet()) {

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
        final ExecutionYear executionYear = process.getExecutionYear();
        final AlertMessage subject =
                AlertMessage
                        .create(AlertMessage.get("message.phd.remind.jury.reviews.subject", process.getPhdProgram().getName(executionYear)))
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
