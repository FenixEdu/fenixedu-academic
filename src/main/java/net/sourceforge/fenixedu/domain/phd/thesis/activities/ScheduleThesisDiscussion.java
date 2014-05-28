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
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

import org.fenixedu.bennu.core.domain.User;

public class ScheduleThesisDiscussion extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.getActiveState().equals(PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING)) {
            throw new PreConditionNotValidException();
        }

        if (process.isAllowedToManageProcess(userView)) {
            return;
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {

        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        if (bean.isToNotify()) {
            notifyJuryElements(process, bean);
            alertStudent(process, bean);
        }

        checkDiscussionInformation(bean);

        process.setDiscussionDate(bean.getScheduledDate());
        process.setDiscussionPlace(bean.getScheduledPlace());

        process.createState(PhdThesisProcessStateType.THESIS_DISCUSSION_DATE_SCHECULED, userView.getPerson(), bean.getRemarks());

        return process;
    }

    private void checkDiscussionInformation(PhdThesisProcessBean bean) {
        if (bean.getScheduledDate() == null) {
            throw new DomainException("error.ScheduleThesisDiscussion.invalid.discussion.date");
        }

        if (bean.getScheduledPlace() == null || bean.getScheduledPlace().isEmpty()) {
            throw new DomainException("error.ScheduleThesisDiscussion.invalid.discussion.place");
        }

    }

    private void alertStudent(PhdThesisProcess process, final PhdThesisProcessBean bean) {

        final AlertMessage subject = AlertMessage.create(bean.getMailSubject()).isKey(false).withPrefix(false);
        final AlertMessage body =
                AlertMessage.create(buildBody(process.getIndividualProgramProcess(), null, bean)).isKey(false).withPrefix(false);

        AlertService.alertStudent(process.getIndividualProgramProcess(), subject, body);
    }

    private void notifyJuryElements(final PhdThesisProcess process, final PhdThesisProcessBean bean) {
        for (final ThesisJuryElement juryElement : process.getThesisJuryElements()) {
            final PhdParticipant participant = juryElement.getParticipant();
            sendAlertToJuryElement(process.getIndividualProgramProcess(), participant, bean);
        }
    }

    private void sendAlertToJuryElement(PhdIndividualProgramProcess process, PhdParticipant participant, PhdThesisProcessBean bean) {
        final AlertMessage subject = AlertMessage.create(bean.getMailSubject()).isKey(false).withPrefix(false);
        final AlertMessage body = AlertMessage.create(buildBody(process, participant, bean)).isKey(false).withPrefix(false);

        AlertService.alertParticipants(process, subject, body, participant);
    }

    private String buildBody(PhdIndividualProgramProcess process, PhdParticipant participant, PhdThesisProcessBean bean) {
        return bean.getMailBody() + "\n\n ---- " + getThesisDiscussionInformation(bean);
    }

    private String getThesisDiscussionInformation(final PhdThesisProcessBean bean) {
        final StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(AlertMessage.get("label.phd.date")).append(": ");
        sb.append(bean.getScheduledDate().toString("dd/MM/yyyy")).append("\n");
        sb.append(AlertMessage.get("label.phd.hour")).append(": ");
        sb.append(bean.getScheduledDate().toString("HH:mm")).append("\n");
        sb.append(AlertMessage.get("label.phd.meeting.place")).append(": ");
        sb.append(bean.getScheduledPlace());

        return sb.toString();
    }

}
