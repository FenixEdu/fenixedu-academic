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
package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdEditMeetingBean;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeeting;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingBean;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeeting;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeetingRequest;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleThesisMeeting;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleThesisMeetingRequest;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.SubmitThesisMeetingMinutes;
import net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdIndividualProgramProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.CommonPhdThesisProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdMeetingSchedulingProcess", module = "academicAdministration",
        functionality = PhdIndividualProgramProcessDA.class)
@Forwards({
        @Forward(name = "requestScheduleFirstThesisMeeting",
                path = "/phd/thesis/academicAdminOffice/requestScheduleFirstThesisMeeting.jsp"),
        @Forward(name = "scheduleFirstThesisMeeting", path = "/phd/thesis/academicAdminOffice/scheduleFirstThesisMeeting.jsp"),
        @Forward(name = "requestScheduleThesisMeeting", path = "/phd/thesis/academicAdminOffice/requestScheduleThesisMeeting.jsp"),
        @Forward(name = "scheduleThesisMeeting", path = "/phd/thesis/academicAdminOffice/scheduleThesisMeeting.jsp"),
        @Forward(name = "submitThesisMeetingMinutes", path = "/phd/thesis/academicAdminOffice/submitThesisMeetingMinutes.jsp"),
        @Forward(name = "viewMeetingSchedulingProcess", path = "/phd/thesis/academicAdminOffice/viewMeetingSchedulingProcess.jsp"),
        @Forward(name = "editMeetingAttributes", path = "/phd/thesis/academicAdminOffice/editMeetingAttributes.jsp") })
public class PhdMeetingSchedulingProcessDA extends CommonPhdThesisProcessDA {

    // Schedule first thesis meeting request
    public ActionForward prepareRequestScheduleFirstThesisMeeting(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("thesisProcessBean", new PhdThesisProcessBean());
        return mapping.findForward("requestScheduleFirstThesisMeeting");
    }

    public ActionForward requestScheduleFirstThesisMeeting(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdThesisProcessBean bean = getRenderedObject("thesisProcessBean");
        try {
            ExecuteProcessActivity.run(getProcess(request).getMeetingProcess(), ScheduleFirstThesisMeetingRequest.class, bean);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("thesisProcessBean", bean);
            return mapping.findForward("requestScheduleThesisMeeting");
        }

        return viewMeetingSchedulingProcess(request, getProcess(request));
    }

    // End of schedule first thesis meeting request

    // Schedule first thesis meeting
    public ActionForward prepareScheduleFirstThesisMeeting(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        final PhdThesisProcess thesisProcess = getProcess(request);

        bean.setThesisProcess(thesisProcess);
        setDefaultMeetingMailInformation(bean, thesisProcess);

        request.setAttribute("thesisProcessBean", bean);
        return mapping.findForward("scheduleFirstThesisMeeting");
    }

    public ActionForward scheduleFirstThesisMeetingInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        return mapping.findForward("scheduleFirstThesisMeeting");
    }

    public ActionForward scheduleFirstThesisMeetingPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("scheduleFirstThesisMeeting");
    }

    public ActionForward scheduleFirstThesisMeeting(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcess thesisProcess = getProcess(request);

        try {
            ExecuteProcessActivity.run(thesisProcess.getMeetingProcess(), ScheduleFirstThesisMeeting.class,
                    getThesisProcessBean());

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return scheduleThesisMeetingInvalid(mapping, actionForm, request, response);
        }

        return viewMeetingSchedulingProcess(request, getProcess(request));
    }

    // End of schedule first thesis meeting

    // Schedule thesis meeting request
    public ActionForward prepareRequestScheduleThesisMeeting(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("thesisProcessBean", new PhdThesisProcessBean());
        return mapping.findForward("requestScheduleThesisMeeting");
    }

    public ActionForward requestScheduleThesisMeeting(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcessBean bean = getRenderedObject("thesisProcessBean");
        try {
            ExecuteProcessActivity.run(getProcess(request).getMeetingProcess(), ScheduleThesisMeetingRequest.class, bean);

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("thesisProcessBean", bean);
            return mapping.findForward("requestScheduleThesisMeeting");
        }

        return viewMeetingSchedulingProcess(request, getProcess(request));

    }

    // End of schedule thesis meeting request

    // Schedule thesis meeting
    @Override
    public ActionForward prepareScheduleThesisMeeting(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        final PhdThesisProcess thesisProcess = getProcess(request);

        bean.setThesisProcess(thesisProcess);
        setDefaultMeetingMailInformation(bean, thesisProcess);

        request.setAttribute("thesisProcessBean", bean);
        return mapping.findForward("scheduleThesisMeeting");
    }

    private void setDefaultMeetingMailInformation(final PhdThesisProcessBean bean, final PhdThesisProcess thesisProcess) {
        final PhdIndividualProgramProcess process = thesisProcess.getIndividualProgramProcess();
        bean.setMailSubject(AlertService
                .getSubjectPrefixed(process, "message.phd.thesis.schedule.thesis.meeting.default.subject"));
        bean.setMailBody(AlertService.getBodyText(process, "message.phd.thesis.schedule.thesis.meeting.default.body"));
    }

    @Override
    public ActionForward scheduleThesisMeetingInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        return mapping.findForward("scheduleThesisMeeting");
    }

    @Override
    public ActionForward scheduleThesisMeetingPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("scheduleThesisMeeting");
    }

    @Override
    public ActionForward scheduleThesisMeeting(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcess thesisProcess = getProcess(request);

        try {
            ExecuteProcessActivity.run(thesisProcess.getMeetingProcess(), ScheduleThesisMeeting.class, getThesisProcessBean());

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return scheduleThesisMeetingInvalid(mapping, actionForm, request, response);
        }

        return viewMeetingSchedulingProcess(request, getProcess(request));
    }

    // End of schedule thesis meeting

    // Submit thesis meeting minutes

    public ActionForward prepareSubmitThesisMeetingMinutes(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdMeetingBean bean = new PhdMeetingBean();
        final PhdMeeting meeting = getPhdMeeting(request);
        bean.setDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_MEETING_MINUTES));
        bean.setMeeting(meeting);
        bean.setMeetingProcess(meeting.getMeetingProcess());

        request.setAttribute("meetingBean", bean);
        return mapping.findForward("submitThesisMeetingMinutes");
    }

    public ActionForward prepareSubmitThesisMeetingMinutesInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        RenderUtils.invalidateViewState("meetingBean.edit.document");
        request.setAttribute("meetingBean", getRenderedObject("meetingBean"));

        return mapping.findForward("submitThesisMeetingMinutes");
    }

    public ActionForward submitThesisMeetingMinutes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        PhdMeetingBean meetingBean = getRenderedObject("meetingBean");
        try {
            ExecuteProcessActivity.run(meetingBean.getMeetingProcess(), SubmitThesisMeetingMinutes.class, meetingBean);
        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return prepareSubmitThesisMeetingMinutesInvalid(mapping, form, request, response);
        }

        return viewMeetingSchedulingProcess(request, getProcess(request));
    }

    private ActionForward viewMeetingSchedulingProcess(HttpServletRequest request, final PhdThesisProcess process) {
        return redirect(
                String.format("/phdThesisProcess.do?method=viewMeetingSchedulingProcess&processId=%s", process.getExternalId()),
                request);
    }

    private PhdMeeting getPhdMeeting(HttpServletRequest request) {
        return getDomainObject(request, "meetingId");
    }

    // End submit thesis meeting minutes

    // Start of viewing Meeting Scheduling Process

    public ActionForward viewMeetingSchedulingProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return viewMeetingSchedulingProcess(request, getProcess(request));
    }

    // End of viewing Meeting Scheduling Process

    public ActionForward prepareEditMeetingAttributes(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        PhdMeeting meeting = getDomainObject(request, "meetingId");
        PhdEditMeetingBean bean = new PhdEditMeetingBean(meeting);

        request.setAttribute("process", getProcess(request));
        request.setAttribute("meeting", meeting);
        request.setAttribute("bean", bean);

        return mapping.findForward("editMeetingAttributes");
    }

    public ActionForward editMeetingAttributes(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        PhdMeeting meeting = getDomainObject(request, "meetingId");
        PhdThesisProcess process = getProcess(request);
        PhdEditMeetingBean bean = getRenderedObject("bean");

        meeting.editAttributes(bean);

        String link = "/phdThesisProcess.do?method=viewMeetingSchedulingProcess&amp;processId=" + process.getExternalId();

        return new ActionForward(link, false);
    }

    public ActionForward editMeetingAttributesInvalid(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        PhdMeeting meeting = getDomainObject(request, "meetingId");
        PhdThesisProcess process = getProcess(request);
        PhdEditMeetingBean bean = getRenderedObject("bean");

        request.setAttribute("process", process);
        request.setAttribute("meeting", meeting);
        request.setAttribute("bean", bean);

        return mapping.findForward("editMeetingAttributes");
    }

}
