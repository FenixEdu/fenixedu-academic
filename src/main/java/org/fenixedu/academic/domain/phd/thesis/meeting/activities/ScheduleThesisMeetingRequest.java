/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessStateType;

import org.fenixedu.bennu.core.domain.User;

public class ScheduleThesisMeetingRequest extends PhdMeetingSchedulingActivity {

    @Override
    protected void activityPreConditions(PhdMeetingSchedulingProcess process, User userView) {

        if (!process.getActiveState().equals(PhdMeetingSchedulingProcessStateType.WITHOUT_THESIS_MEETING_REQUEST)) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdMeetingSchedulingProcess executeActivity(PhdMeetingSchedulingProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
        final PhdThesisProcess thesisProcess = process.getThesisProcess();

        if (bean.isToNotify()) {
            // Alert president jury element
            AlertService.alertParticipants(thesisProcess.getIndividualProgramProcess(), AlertMessage
                    .create("message.phd.request.schedule.meeting.president.notification.subject"), AlertMessage
                    .create("message.phd.request.schedule.meeting.president.notification.body"), thesisProcess
                    .getPresidentJuryElement().getParticipant());

            AlertService.alertResponsibleCoordinators(thesisProcess.getIndividualProgramProcess(), AlertMessage
                    .create("message.phd.request.schedule.meeting.coordinator.notification.subject"), AlertMessage.create(
                    "message.phd.request.schedule.meeting.coordinator.notification.body", thesisProcess.getPresidentJuryElement()
                            .getNameWithTitle()));

        }

        process.createState(PhdMeetingSchedulingProcessStateType.WAITING_THESIS_MEETING_SCHEDULE, userView.getPerson(),
                bean.getRemarks());

        return process;
    }
}
