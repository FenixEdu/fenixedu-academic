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
