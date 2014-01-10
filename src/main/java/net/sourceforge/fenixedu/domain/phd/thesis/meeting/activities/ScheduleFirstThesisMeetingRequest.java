package net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessStateType;

import org.fenixedu.bennu.core.domain.User;

public class ScheduleFirstThesisMeetingRequest extends PhdMeetingSchedulingActivity {

    @Override
    protected void activityPreConditions(PhdMeetingSchedulingProcess process, User userView) {

        if (!process.getThesisProcess().getActiveState().equals(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK)) {
            throw new PreConditionNotValidException();
        }

        if (!process.getActiveState().equals(PhdMeetingSchedulingProcessStateType.WAITING_FIRST_THESIS_MEETING_REQUEST)) {
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

        thesisProcess.createState(PhdThesisProcessStateType.WAITING_FOR_THESIS_MEETING_SCHEDULING, userView.getPerson(),
                bean.getRemarks());
        process.createState(PhdMeetingSchedulingProcessStateType.WAITING_FIRST_THESIS_MEETING_SCHEDULE, userView.getPerson(),
                bean.getRemarks());

        return process;
    }

}
