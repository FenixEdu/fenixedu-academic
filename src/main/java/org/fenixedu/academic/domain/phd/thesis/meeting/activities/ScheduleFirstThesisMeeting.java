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
package org.fenixedu.academic.domain.phd.thesis.meeting.activities;

import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.access.PhdProcessAccessType;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.alert.AlertService.AlertMessage;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessStateType;
import org.fenixedu.academic.domain.phd.thesis.ThesisJuryElement;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeeting;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessStateType;
import org.fenixedu.bennu.core.domain.User;

public class ScheduleFirstThesisMeeting extends PhdMeetingSchedulingActivity {

    @Override
    protected void activityPreConditions(PhdMeetingSchedulingProcess process, User userView) {

        if (!process.getThesisProcess().getActiveState().equals(PhdThesisProcessStateType.WAITING_FOR_THESIS_MEETING_SCHEDULING)) {
            throw new PreConditionNotValidException();
        }

        if (!process.getActiveState().equals(PhdMeetingSchedulingProcessStateType.WAITING_FIRST_THESIS_MEETING_SCHEDULE)) {
            throw new PreConditionNotValidException();
        }

        if (process.isAllowedToManageProcess(userView)) {
            return;
        }

        if (!process.getThesisProcess().isPresidentJuryElement(userView.getPerson())) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdMeetingSchedulingProcess executeActivity(PhdMeetingSchedulingProcess process, User userView, Object object) {

        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
        final PhdThesisProcess thesisProcess = process.getThesisProcess();

        if (bean.isToNotify()) {
            notifyJuryElements(thesisProcess, bean);
            alertAcademicOfficeAndCoordinatorAndGuiders(thesisProcess, bean);
        }

        checkMeetingInformation(bean);

        thesisProcess.setMeetingDate(bean.getScheduledDate());
        thesisProcess.setMeetingPlace(bean.getScheduledPlace());
        thesisProcess.createState(PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING, userView.getPerson(),
                bean.getRemarks());

        process.addMeeting(PhdMeeting.create(process, bean.getScheduledDate(), bean.getScheduledPlace()));
        process.createState(PhdMeetingSchedulingProcessStateType.WITHOUT_THESIS_MEETING_REQUEST, userView.getPerson(),
                bean.getRemarks());

        return process;
    }

    private void checkMeetingInformation(PhdThesisProcessBean bean) {

        if (bean.getScheduledDate() == null) {
            throw new DomainException("error.ScheduleThesisMeeting.invalid.meeting.date");
        }

        if (bean.getScheduledPlace() == null || bean.getScheduledPlace().isEmpty()) {
            throw new DomainException("error.ScheduleThesisMeeting.invalid.meeting.place");
        }
    }

    private void alertAcademicOfficeAndCoordinatorAndGuiders(PhdThesisProcess process, final PhdThesisProcessBean bean) {

        final AlertMessage subject = AlertMessage.create(bean.getMailSubject()).isKey(false).withPrefix(false);
        final AlertMessage body =
                AlertMessage.create(buildBody(process.getIndividualProgramProcess(), null, bean)).isKey(false).withPrefix(false);

        AlertService.alertAcademicOffice(process.getIndividualProgramProcess(), AcademicOperationType.VIEW_PHD_THESIS_ALERTS,
                subject, body);
        AlertService.alertResponsibleCoordinators(process.getIndividualProgramProcess(), subject, body);
        AlertService.alertGuiders(process.getIndividualProgramProcess(), subject, body);

    }

    private void notifyJuryElements(final PhdThesisProcess process, final PhdThesisProcessBean bean) {

        for (final ThesisJuryElement juryElement : process.getThesisJuryElementsSet()) {

            if (!juryElement.isInternal()) {
                createExternalAccess(juryElement.getParticipant());
            }

            final PhdParticipant participant = juryElement.getParticipant();
            sendAlertToJuryElement(process.getIndividualProgramProcess(), participant, bean);
        }

        PhdParticipant presidentParticipant = process.getPresidentJuryElement().getParticipant();
        if (!presidentParticipant.isInternal()) {
            createExternalAccess(presidentParticipant);
        }

        sendAlertToJuryElement(process.getIndividualProgramProcess(), presidentParticipant, bean);
    }

    private void createExternalAccess(final PhdParticipant participant) {
        participant.addAccessType(PhdProcessAccessType.JURY_REVIEW_DOCUMENTS_DOWNLOAD);
    }

    private void sendAlertToJuryElement(PhdIndividualProgramProcess process, PhdParticipant participant, PhdThesisProcessBean bean) {
        final AlertMessage subject = AlertMessage.create(bean.getMailSubject()).isKey(false).withPrefix(false);
        final AlertMessage body = AlertMessage.create(buildBody(process, participant, bean)).isKey(false).withPrefix(false);

        AlertService.alertParticipants(process, subject, body, participant);
    }

    private String buildBody(PhdIndividualProgramProcess process, PhdParticipant participant, PhdThesisProcessBean bean) {
        return bean.getMailBody() + "\n\n ---- " + getScheduledMeetingInformation(bean)
                + appendAccessInformation(process, participant, bean);
    }

    private String appendAccessInformation(PhdIndividualProgramProcess process, PhdParticipant participant,
            PhdThesisProcessBean bean) {
        if (participant == null) {
            return "";
        }

        return "\n\n ----"
                + getAccessInformation(process, participant, "message.phd.thesis.schedule.thesis.meeting.coordinator.access",
                        "message.phd.thesis.schedule.thesis.meeting.teacher.access");

    }

    private String getScheduledMeetingInformation(final PhdThesisProcessBean bean) {
        final StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(AlertMessage.get("label.phd.date")).append(": ");
        sb.append(bean.getScheduledDate().toString("dd/MM/yyyy")).append("\n");
        sb.append(AlertMessage.get("label.phd.hour")).append(": ");
        sb.append(bean.getScheduledDate().toString("HH:mm")).append("\n");
        sb.append(AlertMessage.get("label.phd.meeting.place")).append(": ");
        sb.append(bean.getScheduledPlace());

        return sb.toString();
    }

}
