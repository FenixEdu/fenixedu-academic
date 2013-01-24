package net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessStateType;

public class ScheduleThesisMeetingRequest extends PhdMeetingSchedulingActivity {

    @Override
    protected void activityPreConditions(PhdMeetingSchedulingProcess process, IUserView userView) {

	if (!process.getActiveState().equals(PhdMeetingSchedulingProcessStateType.WITHOUT_THESIS_MEETING_REQUEST)) {
	    throw new PreConditionNotValidException();
	}

	if (!process.isAllowedToManageProcess(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdMeetingSchedulingProcess executeActivity(PhdMeetingSchedulingProcess process, IUserView userView, Object object) {
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
