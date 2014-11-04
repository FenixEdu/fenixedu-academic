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
package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis;

import java.io.IOException;

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
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReporterFeedbackUpload;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.ScheduleThesisMeeting;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdDocumentsZip;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

abstract public class CommonPhdThesisProcessDA extends PhdProcessDA {

    @Override
    protected PhdThesisProcess getProcess(HttpServletRequest request) {
        return (PhdThesisProcess) super.getProcess(request);
    }

    public ActionForward viewIndividualProgramProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return viewIndividualProgramProcess(request, getProcess(request));
    }

    protected ActionForward viewIndividualProgramProcess(HttpServletRequest request, final PhdThesisProcess process) {
        return redirect(String.format("/phdIndividualProgramProcess.do?method=viewProcess&processId=%s", process
                .getIndividualProgramProcess().getExternalId()), request);
    }

    // jury report feedback operations

    public ActionForward prepareJuryReportFeedbackUpload(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK));
        bean.setJuryElement(getProcess(request).getThesisJuryElement(AccessControl.getPerson()));

        request.setAttribute("thesisProcessBean", bean);
        request.setAttribute("thesisDocuments", getProcess(request).getThesisDocumentsToFeedback());

        return mapping.findForward("juryReporterFeedbackUpload");
    }

    public ActionForward juryReportFeedbackUploadInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        request.setAttribute("thesisDocuments", getProcess(request).getThesisDocumentsToFeedback());
        return mapping.findForward("juryReporterFeedbackUpload");
    }

    public ActionForward juryReportFeedbackUpload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        try {

            final IViewState viewState = RenderUtils.getViewState("thesisProcessBean.edit.documents");
            if (!viewState.isValid()) {
                return juryReportFeedbackUploadInvalid(mapping, actionForm, request, response);
            }
            ExecuteProcessActivity.run(getProcess(request), JuryReporterFeedbackUpload.class, getThesisProcessBean());
            addSuccessMessage(request, "message.thesis.jury.report.feedback.uploaded.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return juryReportFeedbackUploadInvalid(mapping, actionForm, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward downloadThesisDocumentsToFeedback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        writeFile(response, getThesisDocumentsToFeedbackFilename(request), PhdDocumentsZip.ZIP_MIME_TYPE,
                PhdDocumentsZip.zip(getProcess(request).getThesisDocumentsToFeedback()));

        return null;
    }

    protected String getThesisDocumentsToFeedbackFilename(HttpServletRequest request) {
        return getProcess(request).getProcessNumber().replace("/", "-") + "-Documents.zip";
    }

    protected PhdThesisProcessBean getThesisProcessBean() {
        return getRenderedObject("thesisProcessBean");
    }

    // end of jury report feedback operations

    // Manage thesis documents

    public ActionForward manageThesisDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("manageThesisDocuments");
    }

    public ActionForward downloadThesisDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        writeFile(response, getThesisDocumentsFilename(request), PhdDocumentsZip.ZIP_MIME_TYPE, createZip(request));
        return null;
    }

    protected String getThesisDocumentsFilename(HttpServletRequest request) {
        final PhdIndividualProgramProcess process = getProcess(request).getIndividualProgramProcess();
        return String.format("%s-%s.zip", process.getProcessNumber().replace("/", "-"),
                getMessageFromResource("label.phd.manageThesisDocuments").replace(" ", "_"));
    }

    public ActionForward deleteDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return executeActivity(net.sourceforge.fenixedu.domain.phd.candidacy.activities.DeleteDocument.class,
                getDomainObject(request, "documentId"), request, mapping, "manageThesisDocuments", "manageThesisDocuments",
                "message.document.deleted.successfuly");
    }

    // End of manage thesis documents

    // Schedule thesis meeting

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

    public ActionForward scheduleThesisMeetingInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        return mapping.findForward("scheduleThesisMeeting");
    }

    public ActionForward scheduleThesisMeetingPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("scheduleThesisMeeting");
    }

    public ActionForward scheduleThesisMeeting(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcess thesisProcess = getProcess(request);

        try {
            ExecuteProcessActivity.run(thesisProcess, ScheduleThesisMeeting.class, getThesisProcessBean());

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return scheduleThesisMeetingInvalid(mapping, actionForm, request, response);
        }

        return viewIndividualProgramProcess(request, thesisProcess);
    }

    // End of schedule thesis meeting
}
