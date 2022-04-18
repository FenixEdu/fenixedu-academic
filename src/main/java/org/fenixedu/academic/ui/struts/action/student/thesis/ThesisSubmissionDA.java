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
package org.fenixedu.academic.ui.struts.action.student.thesis;

import com.google.common.io.ByteStreams;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DefaultDocumentGenerator;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.report.thesis.StudentThesisIdentificationDocument;
import org.fenixedu.academic.service.services.thesis.AcceptThesisDeclaration;
import org.fenixedu.academic.service.services.thesis.CreateThesisAbstractFile;
import org.fenixedu.academic.service.services.thesis.CreateThesisDissertationFile;
import org.fenixedu.academic.service.services.thesis.RejectThesisDeclaration;
import org.fenixedu.academic.ui.struts.action.commons.AbstractManageThesisDA;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentSubmitApp;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.TreeSet;

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

        if (thesis.isWaitingConfirmation()) {
            request.setAttribute("bean", new DeclarationBean(thesis));
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
                    //should not happen since bean.visibility is never null
                    addActionMessage("error", request, "error.student.thesis.declaration.visibility.required");
                    request.setAttribute("bean", new DeclarationBean(thesis));
                    return mapping.findForward("thesis-declaration");
                }
            } else {
                if (thesis.getDissertation() != null || thesis.getExtendedAbstract() != null) {
                    request.setAttribute("confirmRejectWithFiles", true);
                    request.setAttribute("bean", bean);
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
            byte[] data = DefaultDocumentGenerator.getGenerator().generateReport(Collections.singletonList(document));
            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", document.getReportFileName()));

            response.getOutputStream().write(data);

            return null;
        } catch (Exception e) {
            addActionMessage("error", request, "student.thesis.generate.identification.failed");
            return prepareThesisSubmission(mapping, actionForm, request, response);
        }
    }

    @Override
    public ActionForward editProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        throw new Error("this.cannot.be.called.here");
    }

}
