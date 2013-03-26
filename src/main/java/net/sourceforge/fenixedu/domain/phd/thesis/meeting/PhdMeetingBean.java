package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;

import org.joda.time.DateTime;

public class PhdMeetingBean implements Serializable {

    private static final long serialVersionUID = 6530997068171520705L;

    private PhdMeetingSchedulingProcess meetingProcess;

    private PhdMeeting meeting;

    private PhdProgramDocumentUploadBean document;

    private boolean toNotify = true;

    public PhdMeetingBean() {

    }

    public PhdMeetingBean(final PhdMeetingSchedulingProcess meetingProcess) {
        setMeetingProcess(meetingProcess);
    }

    public PhdMeetingSchedulingProcess getMeetingProcess() {
        return meetingProcess;
    }

    public void setMeetingProcess(PhdMeetingSchedulingProcess meetingProcess) {
        this.meetingProcess = meetingProcess;
    }

    public PhdMeeting getMeeting() {
        return meeting;
    }

    public void setMeeting(PhdMeeting meeting) {
        this.meeting = meeting;
    }

    public DateTime getMeetingDate() {
        return this.meeting.getMeetingDate();
    }

    public String getMeetingPlace() {
        return this.meeting.getMeetingPlace();
    }

    public void setDocument(PhdProgramDocumentUploadBean document) {
        this.document = document;
    }

    public PhdProgramDocumentUploadBean getDocument() {
        return this.document;
    }

    public boolean isToNotify() {
        return toNotify;
    }

    public void setToNotify(boolean toNotify) {
        this.toNotify = toNotify;
    }
}
