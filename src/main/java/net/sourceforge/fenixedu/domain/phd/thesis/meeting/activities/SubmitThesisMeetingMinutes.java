package net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeeting;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingBean;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessStateType;

public class SubmitThesisMeetingMinutes extends PhdMeetingSchedulingActivity {

    @Override
    protected void activityPreConditions(PhdMeetingSchedulingProcess process, IUserView userView) {

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

        if (!process.hasState(PhdMeetingSchedulingProcessStateType.WITHOUT_THESIS_MEETING_REQUEST)) {
            throw new PreConditionNotValidException();
        }

        if (!process.getThesisProcess().hasState(PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdMeetingSchedulingProcess executeActivity(PhdMeetingSchedulingProcess process, IUserView userView, Object object) {
        final PhdMeetingBean bean = (PhdMeetingBean) object;
        final PhdMeeting meeting = bean.getMeeting();

        final PhdProgramDocumentUploadBean document = bean.getDocument();
        if (document.hasAnyInformation()) {
            meeting.addDocument(document, userView.getPerson());
        }

        if (bean.isToNotify()) {
            /*
             * TODO: (check subject and body) AlertService.alertStudent(process,
             * subject, body);
             */
        }

        return process;
    }

}
