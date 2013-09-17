package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.PhdMeetingSchedulingActivity;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeeting;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeetingRequest;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleThesisMeeting;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleThesisMeetingRequest;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.SkipScheduleFirstThesisMeeting;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.SubmitThesisMeetingMinutes;

public class PhdMeetingSchedulingProcess extends PhdMeetingSchedulingProcess_Base {

    @StartActivity
    static public class StartThesisMeetings extends PhdMeetingSchedulingActivity {

        @Override
        protected void activityPreConditions(PhdMeetingSchedulingProcess process, IUserView userView) {
            // Activity on main process ensures access control
        }

        @Override
        protected PhdMeetingSchedulingProcess executeActivity(PhdMeetingSchedulingProcess process, IUserView userView,
                Object object) {

            final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
            final PhdMeetingSchedulingProcess result = new PhdMeetingSchedulingProcess(bean.getThesisProcess());

            result.createState(PhdMeetingSchedulingProcessStateType.WAITING_FIRST_THESIS_MEETING_REQUEST, userView.getPerson(),
                    bean.getRemarks());

            return result;
        }

    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
        activities.add(new ScheduleFirstThesisMeetingRequest());
        activities.add(new ScheduleFirstThesisMeeting());
        activities.add(new ScheduleThesisMeetingRequest());
        activities.add(new ScheduleThesisMeeting());
        activities.add(new SubmitThesisMeetingMinutes());

        activities.add(new SkipScheduleFirstThesisMeeting());
    }

    @Override
    public boolean isAllowedToManageProcess(IUserView userView) {
        return this.getIndividualProgramProcess().isAllowedToManageProcess(userView);
    }

    public PhdMeetingSchedulingProcess(PhdThesisProcess process) {
        super();
        super.setThesisProcess(process);
    }

    @Override
    public void setThesisProcess(PhdThesisProcess thesisProcess) {
        throw new DomainException("error.phd.thesis.meeting.cannot.set.thesis.process");
    }

    @Override
    protected Person getPerson() {
        return getIndividualProgramProcess().getPerson();
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
        return false;
    }

    @Override
    public String getDisplayName() {
        return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    @Override
    public PhdMeetingSchedulingProcessStateType getActiveState() {
        return (PhdMeetingSchedulingProcessStateType) super.getActiveState();
    }

    @Override
    protected PhdIndividualProgramProcess getIndividualProgramProcess() {
        return getThesisProcess().getIndividualProgramProcess();
    }

    public void createState(PhdMeetingSchedulingProcessStateType type, Person person, String remarks) {
        PhdMeetingSchedulingProcessState.createWithInferredStateDate(this, type, person, remarks);
    }

    public void addMeeting(PhdMeeting meeting) {
        getMeetings().add(meeting);
    }

    public boolean isAnyMinutesDocumentMissing() {
        for (PhdMeeting meeting : getMeetings()) {
            if (meeting.hasAnyDocuments()) {
                continue;
            }

            return true;
        }

        return false;
    }

    @Override
    public PhdMeetingSchedulingProcessState getMostRecentState() {
        return (PhdMeetingSchedulingProcessState) super.getMostRecentState();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessState> getStates() {
        return getStatesSet();
    }

    @Deprecated
    public boolean hasAnyStates() {
        return !getStatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeeting> getMeetings() {
        return getMeetingsSet();
    }

    @Deprecated
    public boolean hasAnyMeetings() {
        return !getMeetingsSet().isEmpty();
    }

    @Deprecated
    public boolean hasThesisProcess() {
        return getThesisProcess() != null;
    }

}
