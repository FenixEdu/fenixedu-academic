package net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeeting;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessStateType;

public class SkipScheduleFirstThesisMeeting extends PhdMeetingSchedulingActivity {

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

        thesisProcess.setMeetingDate(bean.getScheduledDate());
        thesisProcess.setMeetingPlace(bean.getScheduledPlace());
        thesisProcess.createState(PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING, userView.getPerson(),
                bean.getRemarks());

        process.addMeeting(PhdMeeting.create(process, bean.getScheduledDate(), bean.getScheduledPlace()));
        process.createState(PhdMeetingSchedulingProcessStateType.WITHOUT_THESIS_MEETING_REQUEST, userView.getPerson(),
                bean.getRemarks());

        return process;
    }

}
