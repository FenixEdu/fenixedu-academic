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
package net.sourceforge.fenixedu.presentationTier.Action.student.thesis;

import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.AcceptThesisDeclaration;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.CreateThesisAbstractFile;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.CreateThesisDissertationFile;
import net.sourceforge.fenixedu.applicationTier.Servico.thesis.RejectThesisDeclaration;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.commons.AbstractManageThesisDA;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentSubmitApp;
import net.sourceforge.fenixedu.presentationTier.docs.thesis.StudentThesisIdentificationDocument;
import net.sourceforge.fenixedu.presentationTier.docs.thesis.ThesisJuryReportDocument;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.io.ByteStreams;

@StrutsFunctionality(app = StudentSubmitApp.class, path = "thesis", titleKey = "label.student.thesis.submission")
@Mapping(path = "/thesisSubmission", module = "student")
@Forwards({ @Forward(name = "thesis-notFound", path = "/student/thesis/notFound.jsp"),
        @Forward(name = "thesis-showState", path = "/student/thesis/showState.jsp"),
        @Forward(name = "thesis-showUnavailable", path = "/student/thesis/showUnavailable.jsp"),
        @Forward(name = "thesis-submit", path = "/student/thesis/submit.jsp"),
        @Forward(name = "thesis-edit-abstract", path = "/student/thesis/editAbstract.jsp"),
        @Forward(name = "thesis-edit-keywords", path = "/student/thesis/editKeywords.jsp"),
        @Forward(name = "thesis-declaration", path = "/student/thesis/declaration.jsp"),
        @Forward(name = "thesis-declaration-view", path = "/student/thesis/viewDeclaration.jsp"),
        @Forward(name = "thesis-upload-dissertation", path = "/student/thesis/uploadDissertation.jsp"),
        @Forward(name = "thesis-upload-abstract", path = "/student/thesis/uploadAbstract.jsp"),
        @Forward(name = "thesis-list-enrolments", path = "/student/thesis/listEnrolments.jsp"),
        @Forward(name = "viewOperationsThesis", path = "/student/thesis/viewOperationsThesis.jsp") })
public class ThesisSubmissionDA extends AbstractManageThesisDA {

    public Student getStudent() {
        return Authenticate.getUser().getPerson().getStudent();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("thesis", getThesis(request));

        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public Thesis getThesis(HttpServletRequest request) {
        Thesis thesis = null;

        String idString = request.getParameter("thesisId");
        if (idString == null) {
            thesis = (Thesis) request.getAttribute("thesis");

            if (thesis == null) {
                Student student = getStudent();

                Enrolment enrolment = student.getDissertationEnrolment();
                if (enrolment != null) {
                    thesis = enrolment.getThesis();
                }
            }
        } else {
            thesis = FenixFramework.getDomainObject(idString);
        }

        return thesis;
    }

    @EntryPoint
    public ActionForward listThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Student student = getStudent();
        final TreeSet<Enrolment> enrolments = student.getDissertationEnrolments(null);
        request.setAttribute("enrolments", enrolments);
        return mapping.findForward("thesis-list-enrolments");
    }

    public ActionForward prepareThesisSubmissionByEnrolment(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Enrolment enrolment = getDomainObject(request, "enrolmentId");
        if (enrolment == null) {
            request.setAttribute("noEnrolment", true);
            return mapping.findForward("thesis-notFound");
        }

        Thesis thesis = enrolment.getThesis();
        return prepareThesisSubmission(mapping, request, response, thesis);
    }

    public ActionForward prepareThesisSubmission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        return prepareThesisSubmission(mapping, request, response, thesis);
    }

    private ActionForward prepareThesisSubmission(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, Thesis thesis) throws Exception {
        if (thesis == null || thesis.isDraft() || thesis.isSubmitted()) {
            request.setAttribute("noThesis", true);
            return mapping.findForward("thesis-notFound");
        }
        request.setAttribute("thesis", thesis);
        if (thesis.isWaitingConfirmation()) {
            setupStudentTodo(request, thesis);
            return mapping.findForward("thesis-submit");
        } else {
            if (thesis.isConfirmed() || thesis.isEvaluated()) {
                return mapping.findForward("thesis-showState");
            } else {
                return mapping.findForward("thesis-showUnavailable");
            }
        }
    }

    private void setupStudentTodo(HttpServletRequest request, Thesis thesis) {
        request.setAttribute("todo", thesis.getStudentConditions());
    }

    public ActionForward changeThesisDetails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("changeDetails", true);

        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    public ActionForward editAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return editASCII(mapping, request, "thesis-edit-abstract");
    }

    public ActionForward editKeywords(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return editASCII(mapping, request, "thesis-edit-keywords");
    }

    public ActionForward editASCII(ActionMapping mapping, HttpServletRequest request, String forward) throws Exception {
        Thesis thesis = getThesis(request);
        if (thesis == null) {
            return mapping.findForward("thesis-notFound");
        }

        request.setAttribute("thesis", thesis);
        return mapping.findForward(forward);
    }

    public ActionForward viewDeclaration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);
        request.setAttribute("bean", new DeclarationBean(thesis));

        if (thesis.isWaitingConfirmation()) {
            return mapping.findForward("thesis-declaration");
        } else {
            return mapping.findForward("thesis-declaration-view");
        }
    }

    public ActionForward changeDeclaration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        boolean confirmation = request.getParameter("confirmReject") != null;
        if (confirmation) {
            RejectThesisDeclaration.runRejectThesisDeclaration(thesis);
        } else {
            DeclarationBean bean = getRenderedObject("declarationBean");

            boolean accepted = request.getParameter("accept") != null;
            if (accepted) {
                if (bean.getVisibility() != null) {
                    AcceptThesisDeclaration.runAcceptThesisDeclaration(thesis, bean.getVisibility(), bean.getAvailableAfter());
                } else {
                    if (bean.getVisibility() == null) {
                        addActionMessage("error", request, "error.student.thesis.declaration.visibility.required");
                    }

                    return mapping.findForward("thesis-declaration");
                }
            } else {
                if (thesis.hasDissertation() || thesis.hasExtendedAbstract()) {
                    request.setAttribute("confirmRejectWithFiles", true);
                    return mapping.findForward("thesis-declaration");
                } else {
                    RejectThesisDeclaration.runRejectThesisDeclaration(thesis);
                }
            }
        }

        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    public ActionForward prepareUploadDissertation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Thesis thesis = getThesis(request);
        request.setAttribute("fileBean", new ThesisFileBean(thesis));
        return mapping.findForward("thesis-upload-dissertation");
    }

    public ActionForward uploadDissertation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisFileBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();

        if (!checkContentDisclaimer(request)) {
            addActionMessage("error", request, "label.student.thesis.upload.dissertation.message.content.decline.disclaimer");
            request.setAttribute("fileBean", bean);
            return mapping.findForward("thesis-upload-dissertation");
        }

        if (bean != null && bean.getFile() != null) {
            byte[] bytes = ByteStreams.toByteArray(bean.getFile());
            try {
                CreateThesisDissertationFile.runCreateThesisDissertationFile(getThesis(request), bytes, bean.getSimpleFileName(),
                        bean.getTitle(), bean.getSubTitle(), bean.getLanguage());
            } catch (DomainException e) {
                addActionMessage("error", request, e.getKey(), e.getArgs());
                return prepareUploadDissertation(mapping, actionForm, request, response);
            }
        }

        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    private boolean checkContentDisclaimer(HttpServletRequest request) {
        String contentDisclaimer = request.getParameter("contentDisclaimer");
        if ("checked".equals(contentDisclaimer) || "on".equals(contentDisclaimer)) {
            return true;
        }
        contentDisclaimer = (String) request.getAttribute("contentDisclaimer");
        return "checked".equals(contentDisclaimer) || "on".equals(contentDisclaimer);
    }

    public ActionForward removeDissertation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CreateThesisDissertationFile.runCreateThesisDissertationFile(getThesis(request), null, null, null, null, null);

        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    public ActionForward prepareUploadAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("fileBean", new ThesisFileBean());
        return mapping.findForward("thesis-upload-abstract");
    }

    public ActionForward uploadAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ThesisFileBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();

        if (bean != null && bean.getFile() != null) {
            byte[] bytes = ByteStreams.toByteArray(bean.getFile());
            try {
                CreateThesisAbstractFile.runCreateThesisAbstractFile(getThesis(request), bytes, bean.getSimpleFileName(), null,
                        null, null);
            } catch (DomainException e) {
                addActionMessage("error", request, e.getKey(), e.getArgs());
                return prepareUploadAbstract(mapping, actionForm, request, response);
            }
        }

        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    public ActionForward removeAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CreateThesisAbstractFile.runCreateThesisAbstractFile(getThesis(request), null, null, null, null, null);

        return prepareThesisSubmission(mapping, actionForm, request, response);
    }

    public ActionForward downloadIdentificationSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        try {
            StudentThesisIdentificationDocument document = new StudentThesisIdentificationDocument(thesis);
            byte[] data = ReportsUtils.exportToProcessedPdfAsByteArray(document);

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

            response.getOutputStream().write(data);

            return null;
        } catch (JRException e) {
            addActionMessage("error", request, "student.thesis.generate.identification.failed");
            return prepareThesisSubmission(mapping, actionForm, request, response);
        }
    }

    public ActionForward downloadJuryReportSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        try {
            ThesisJuryReportDocument document = new ThesisJuryReportDocument(thesis);
            byte[] data = ReportsUtils.exportToProcessedPdfAsByteArray(document);

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

            response.getOutputStream().write(data);

            return null;
        } catch (JRException e) {
            addActionMessage("error", request, "student.thesis.generate.juryreport.failed");
            return prepareThesisSubmission(mapping, actionForm, request, response);
        }
    }

    @Override
    public ActionForward editProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        throw new Error("this.cannot.be.called.here");
    }

}
