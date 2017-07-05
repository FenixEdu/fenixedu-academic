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
/**
 * 
 */
package org.fenixedu.academic.domain.phd.thesis.activities;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.caseHandling.Process;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.access.PhdProcessAccessType;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.alert.AlertService.AlertMessage;
import org.fenixedu.academic.domain.phd.alert.PhdReporterReviewAlert;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType;
import org.fenixedu.academic.domain.phd.thesis.ThesisJuryElement;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class RequestJuryReviews extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

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
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {

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
        if (process.getMeetingProcess() == null) {
            Process.createNewProcess(userView, PhdMeetingSchedulingProcess.class, bean);
        }

        return process;
    }

    private void notifyJuryElements(PhdThesisProcess process) {

        for (final ThesisJuryElement juryElement : process.getThesisJuryElementsSet()) {

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

        for (final ThesisJuryElement juryElement : process.getThesisJuryElementsSet()) {
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

        final ExecutionYear executionYear = process.getExecutionYear();
        final AlertMessage subject =
                AlertMessage
                        .create(AlertMessage.get("message.phd.request.jury.reviews.external.access.subject", process
                                .getPhdProgram().getName(executionYear).getContent())).isKey(false).withPrefix(false);

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
        final ExecutionYear executionYear = process.getExecutionYear();
        final AlertMessage subject =
                AlertMessage
                        .create(AlertMessage.get("message.phd.request.jury.reviews.external.access.subject", process
                                .getPhdProgram().getName(executionYear).getContent())).isKey(false).withPrefix(false);

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