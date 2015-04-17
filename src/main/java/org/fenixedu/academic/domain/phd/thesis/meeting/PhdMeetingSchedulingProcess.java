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
package org.fenixedu.academic.domain.phd.thesis.meeting;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.caseHandling.StartActivity;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.academic.domain.phd.thesis.meeting.activities.PhdMeetingSchedulingActivity;
import org.fenixedu.academic.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeeting;
import org.fenixedu.academic.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeetingRequest;
import org.fenixedu.academic.domain.phd.thesis.meeting.activities.ScheduleThesisMeeting;
import org.fenixedu.academic.domain.phd.thesis.meeting.activities.ScheduleThesisMeetingRequest;
import org.fenixedu.academic.domain.phd.thesis.meeting.activities.SkipScheduleFirstThesisMeeting;
import org.fenixedu.academic.domain.phd.thesis.meeting.activities.SubmitThesisMeetingMinutes;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class PhdMeetingSchedulingProcess extends PhdMeetingSchedulingProcess_Base {

    @StartActivity
    static public class StartThesisMeetings extends PhdMeetingSchedulingActivity {

        @Override
        protected void activityPreConditions(PhdMeetingSchedulingProcess process, User userView) {
            // Activity on main process ensures access control
        }

        @Override
        protected PhdMeetingSchedulingProcess executeActivity(PhdMeetingSchedulingProcess process, User userView, Object object) {

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
    public boolean isAllowedToManageProcess(User userView) {
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
    public boolean canExecuteActivity(User userView) {
        return false;
    }

    @Override
    public String getDisplayName() {
        return BundleUtil.getString(Bundle.PHD, getClass().getSimpleName());
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
        getMeetingsSet().add(meeting);
    }

    public boolean isAnyMinutesDocumentMissing() {
        for (PhdMeeting meeting : getMeetingsSet()) {
            if (!meeting.getDocumentsSet().isEmpty()) {
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

    @Override
    public java.util.Set<org.fenixedu.academic.domain.phd.thesis.meeting.PhdMeetingSchedulingProcessState> getStates() {
        return getStatesSet();
    }

    @Override
    public boolean hasAnyStates() {
        return !getStatesSet().isEmpty();
    }

}
