package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

public class ScheduleThesisMeetingRequest extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.getActiveState().equals(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK)) {
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

            // Alert president jury element
            AlertService.alertParticipants(process.getIndividualProgramProcess(), AlertMessage
                    .create("message.phd.request.schedule.meeting.president.notification.subject"), AlertMessage
                    .create("message.phd.request.schedule.meeting.president.notification.body"), process
                    .getPresidentJuryElement().getParticipant());

            AlertService.alertResponsibleCoordinators(process.getIndividualProgramProcess(), AlertMessage
                    .create("message.phd.request.schedule.meeting.coordinator.notification.subject"), AlertMessage.create(
                    "message.phd.request.schedule.meeting.coordinator.notification.body", process.getPresidentJuryElement()
                            .getNameWithTitle()));

        }

        process.createState(PhdThesisProcessStateType.WAITING_FOR_THESIS_MEETING_SCHEDULING, userView.getPerson(),
                bean.getRemarks());

        return process;
    }
}
