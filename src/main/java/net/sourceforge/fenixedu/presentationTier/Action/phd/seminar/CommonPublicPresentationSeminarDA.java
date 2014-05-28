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
package net.sourceforge.fenixedu.presentationTier.Action.phd.seminar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.RejectComission;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.RejectReport;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.SchedulePresentationDate;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.SubmitComission;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.UploadReport;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.ValidateComission;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.ValidateReport;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

abstract public class CommonPublicPresentationSeminarDA extends PhdProcessDA {

    @Override
    protected PublicPresentationSeminarProcess getProcess(HttpServletRequest request) {
        return (PublicPresentationSeminarProcess) super.getProcess(request);
    }

    public ActionForward viewIndividualProgramProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return viewIndividualProgramProcess(request, getProcess(request));
    }

    protected ActionForward viewIndividualProgramProcess(HttpServletRequest request,
            final PublicPresentationSeminarProcess process) {
        return redirect(String.format("/phdIndividualProgramProcess.do?method=viewProcess&processId=%s", process
                .getIndividualProgramProcess().getExternalId()), request);
    }

    // submit comission
    public ActionForward prepareSubmitComission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PublicPresentationSeminarProcessBean submitComissionBean =
                new PublicPresentationSeminarProcessBean(getProcess(request).getIndividualProgramProcess());
        final PhdProgramDocumentUploadBean documentBean =
                new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_COMISSION);
        submitComissionBean.setDocument(documentBean);

        request.setAttribute("submitComissionBean", submitComissionBean);

        return mapping.findForward("submitComission");

    }

    public ActionForward prepareSubmitComissionInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("submitComissionBean", getRenderedObject("submitComissionBean"));

        return mapping.findForward("submitComission");
    }

    public ActionForward submitComission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), SubmitComission.class, getRenderedObject("submitComissionBean"));

            addSuccessMessage(request, "message.comission.submitted.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("submitComission");
        }

        return viewIndividualProgramProcess(request, getProcess(request));

    }

    // end of submit comission

    // validate comission
    public ActionForward prepareValidateComission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PublicPresentationSeminarProcessBean validateComissionBean =
                new PublicPresentationSeminarProcessBean(getProcess(request).getIndividualProgramProcess());
        final PhdProgramDocumentUploadBean documentBean =
                new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.PUBLIC_PRESENTATION_SEMINAR_COMISSION);
        validateComissionBean.setDocument(documentBean);

        request.setAttribute("validateComissionBean", validateComissionBean);

        return mapping.findForward("validateComission");

    }

    public ActionForward prepareValidateComissionInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("validateComissionBean", getRenderedObject("validateComissionBean"));

        return mapping.findForward("validateComission");
    }

    public ActionForward validateComission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), ValidateComission.class, getRenderedObject("validateComissionBean"));

            addSuccessMessage(request, "message.comission.validated.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("validateComission");
        }

        return viewIndividualProgramProcess(request, getProcess(request));

    }

    public ActionForward rejectComission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), RejectComission.class, getRenderedObject("validateComissionBean"));

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("validateComission");
        }

        return viewIndividualProgramProcess(request, getProcess(request));

    }

    // end of validate comission

    // schedule presentation date
    public ActionForward prepareSchedulePresentationDate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PublicPresentationSeminarProcessBean schedulePresentationDateBean =
                new PublicPresentationSeminarProcessBean(getProcess(request).getIndividualProgramProcess());

        request.setAttribute("schedulePresentationDateBean", schedulePresentationDateBean);

        return mapping.findForward("schedulePresentationDate");

    }

    public ActionForward prepareSchedulePresentationDateInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("schedulePresentationDateBean", getRenderedObject("schedulePresentationDateBean"));

        return mapping.findForward("schedulePresentationDate");
    }

    public ActionForward schedulePresentationDate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), SchedulePresentationDate.class,
                    getRenderedObject("schedulePresentationDateBean"));

            addSuccessMessage(request, "message.presentation.date.scheduled.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("schedulePresentationDate");
        }

        return viewIndividualProgramProcess(request, getProcess(request));

    }

    // end of schedule presentation date

    // upload report
    public ActionForward prepareUploadReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PublicPresentationSeminarProcessBean uploadReportBean =
                new PublicPresentationSeminarProcessBean(getProcess(request).getIndividualProgramProcess());

        request.setAttribute("uploadReportBean", uploadReportBean);

        return mapping.findForward("uploadReport");

    }

    public ActionForward prepareUploadReportInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("uploadReportBean", getRenderedObject("uploadReportBean"));

        return mapping.findForward("uploadReport");
    }

    public ActionForward uploadReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), UploadReport.class, getRenderedObject("uploadReportBean"));

            addSuccessMessage(request, "message.public.presentation.seminar.report.uploaded.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("uploadReport");
        }

        return viewIndividualProgramProcess(request, getProcess(request));

    }

    // end of upload report

    // validate report
    public ActionForward prepareValidateReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PublicPresentationSeminarProcessBean validateReportBean =
                new PublicPresentationSeminarProcessBean(getProcess(request).getIndividualProgramProcess());

        request.setAttribute("validateReportBean", validateReportBean);

        return mapping.findForward("validateReport");

    }

    public ActionForward prepareValidateReportInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("validateReportBean", getRenderedObject("validateReportBean"));

        return mapping.findForward("validateReport");
    }

    public ActionForward validateReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), ValidateReport.class, getRenderedObject("validateReportBean"));

            addSuccessMessage(request, "message.public.presentation.seminar.report.validated.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("validateReport");
        }

        return viewIndividualProgramProcess(request, getProcess(request));

    }

    public ActionForward rejectReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), RejectReport.class, getRenderedObject("validateReportBean"));

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("validateReport");
        }

        return viewIndividualProgramProcess(request, getProcess(request));

    }

    // end of validate report

}
