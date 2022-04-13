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
package org.fenixedu.academic.ui.struts.action.phd.thesis.academicAdminOffice;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.*;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.conclusion.PhdConclusionProcessBean;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisJuryElementBean;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcess;
import org.fenixedu.academic.domain.phd.thesis.PhdThesisProcessBean;
import org.fenixedu.academic.domain.phd.thesis.ThesisJuryElement;
import org.fenixedu.academic.domain.phd.thesis.activities.*;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DefaultDocumentGenerator;
import org.fenixedu.academic.report.phd.thesis.PhdThesisJuryElementsDocument;
import org.fenixedu.academic.service.services.caseHandling.ExecuteProcessActivity;
import org.fenixedu.academic.ui.struts.action.phd.PhdProcessStateBean;
import org.fenixedu.academic.ui.struts.action.phd.academicAdminOffice.PhdIndividualProgramProcessDA;
import org.fenixedu.academic.ui.struts.action.phd.thesis.CommonPhdThesisProcessDA;
import org.fenixedu.academic.util.Pair;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.LocalDate;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mapping(path = "/phdThesisProcess", module = "academicAdministration", functionality = PhdIndividualProgramProcessDA.class)
@Forwards({
        @Forward(name = "requestJuryElements", path = "/phd/thesis/academicAdminOffice/requestJuryElements.jsp"),
        @Forward(name = "submitJuryElementsDocument", path = "/phd/thesis/academicAdminOffice/submitJuryElementsDocument.jsp"),
        @Forward(name = "manageThesisJuryElements", path = "/phd/thesis/academicAdminOffice/manageThesisJuryElements.jsp"),
        @Forward(name = "rejectJuryElements", path = "/phd/thesis/academicAdminOffice/rejectJuryElements.jsp"),
        @Forward(name = "addJuryElement", path = "/phd/thesis/academicAdminOffice/addJuryElement.jsp"),
        @Forward(name = "editJuryElement", path = "/phd/thesis/academicAdminOffice/editJuryElement.jsp"),
        @Forward(name = "validateJury", path = "/phd/thesis/academicAdminOffice/validateJury.jsp"),
        @Forward(name = "submitThesis", path = "/phd/thesis/academicAdminOffice/submitThesis.jsp"),
        @Forward(name = "requestJuryReviews", path = "/phd/thesis/academicAdminOffice/requestJuryReviews.jsp"),
        @Forward(name = "remindJuryReviews", path = "/phd/thesis/academicAdminOffice/remindJuryReviews.jsp"),
        @Forward(name = "addPresidentJuryElement", path = "/phd/thesis/academicAdminOffice/addPresidentJuryElement.jsp"),
        @Forward(name = "manageThesisDocuments", path = "/phd/thesis/academicAdminOffice/manageThesisDocuments.jsp"),
        @Forward(name = "scheduleThesisDiscussion", path = "/phd/thesis/academicAdminOffice/scheduleThesisDiscussion.jsp"),
        @Forward(name = "ratifyFinalThesis", path = "/phd/thesis/academicAdminOffice/ratifyFinalThesis.jsp"),
        @Forward(name = "setFinalGrade", path = "/phd/thesis/academicAdminOffice/setFinalGrade.jsp"),
        @Forward(name = "rejectJuryElementsDocuments", path = "/phd/thesis/academicAdminOffice/rejectJuryElementsDocuments.jsp"),
        @Forward(name = "viewMeetingSchedulingProcess", path = "/phd/thesis/academicAdminOffice/viewMeetingSchedulingProcess.jsp"),
        @Forward(name = "juryReporterFeedbackUpload", path = "/phd/thesis/academicAdminOffice/juryReporterFeedbackUpload.jsp"),
        @Forward(name = "replaceDocument", path = "/phd/thesis/academicAdminOffice/replaceDocument.jsp"),
        @Forward(name = "manageStates", path = "/phd/thesis/academicAdminOffice/manageStates.jsp"),
        @Forward(name = "editPhdThesisProcessInformation",
                path = "/phd/thesis/academicAdminOffice/editPhdThesisProcessInformation.jsp"),
        @Forward(name = "listConclusionProcess", path = "/phd/thesis/academicAdminOffice/conclusion/listConclusionProcess.jsp"),
        @Forward(name = "createConclusionProcess",
                path = "/phd/thesis/academicAdminOffice/conclusion/createConclusionProcess.jsp"),
        @Forward(name = "editPhdProcessState", path = "/phd/thesis/academicAdminOffice/editState.jsp"),
        @Forward(name = "viewLogs", path = "/phd/thesis/academicAdminOffice/logs/viewLogs.jsp") })
public class PhdThesisProcessDA extends CommonPhdThesisProcessDA {

    // Begin thesis jury elements management

    public ActionForward prepareRequestJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        request.setAttribute("thesisProcessBean", bean);
        return mapping.findForward("requestJuryElements");
    }

    public ActionForward requestJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        try {

            ExecuteProcessActivity.run(getProcess(request), RequestJuryElements.class, getRenderedObject("thesisProcessBean"));
            addSuccessMessage(request, "message.thesis.jury.elements.requested.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("thesisProcessBean", getRenderedObject("thesisProcessBean"));
            return mapping.findForward("requestJuryElements");
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward prepareSubmitJuryElementsDocument(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final PhdThesisProcessBean bean = new PhdThesisProcessBean(getProcess(request).getIndividualProgramProcess());
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_ELEMENTS));
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_PRESIDENT_ELEMENT));
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_PRESIDENT_DECLARATION));
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.MAXIMUM_GRADE_GUIDER_PROPOSAL));

        return prepareSubmitJuryElementsDocument(mapping, actionForm, request, response, bean);
    }

    public ActionForward prepareSubmitJuryElementsDocument(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, PhdThesisProcessBean bean) {
        request.setAttribute("thesisProcessBean", bean);

        return mapping.findForward("submitJuryElementsDocument");
    }

    public ActionForward submitJuryElementsDocumentInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getRenderedObject("thesisProcessBean"));
        return mapping.findForward("submitJuryElementsDocument");
    }

    public ActionForward submitJuryElementsDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        try {

            final IViewState viewState = RenderUtils.getViewState("thesisProcessBean.edit.documents");
            if (!viewState.isValid()) {
                RenderUtils.invalidateViewState("thesisProcessBean.edit.documents");
                return submitJuryElementsDocumentInvalid(mapping, actionForm, request, response);
            }
            ExecuteProcessActivity.run(getProcess(request), SubmitJuryElementsDocuments.class,
                    getRenderedObject("thesisProcessBean"));
            addSuccessMessage(request, "message.thesis.jury.elements.added.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            RenderUtils.invalidateViewState("thesisProcessBean.edit.documents");
            return submitJuryElementsDocumentInvalid(mapping, actionForm, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward manageThesisJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("manageThesisJuryElements");
    }

    public ActionForward prepareAddJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisJuryElementBean", new PhdThesisJuryElementBean(getProcess(request)));
        return mapping.findForward("addJuryElement");
    }

    public ActionForward prepareAddJuryElementInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
        return mapping.findForward("addJuryElement");
    }

    public ActionForward prepareAddJuryElementPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
        RenderUtils.invalidateViewState();
        return mapping.findForward("addJuryElement");
    }

    public ActionForward addJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), AddJuryElement.class, getRenderedObject("thesisJuryElementBean"));
            addSuccessMessage(request, "message.thesis.added.jury.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return prepareAddJuryElementInvalid(mapping, actionForm, request, response);
        }

        return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    static public class ExistingPhdParticipantsEvenInPhdThesisProcess implements DataProvider {

        @Override
        public Converter getConverter() {
            return new DomainObjectKeyConverter();
        }

        @Override
        public Object provide(Object source, Object currentValue) {
            final PhdThesisJuryElementBean bean = (PhdThesisJuryElementBean) source;
            return bean.getExistingParticipants();
        }
    }

    public ActionForward prepareEditJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("thesisJuryElementBean", new PhdThesisJuryElementBean(getProcess(request), getJuryElement(request)));
        return mapping.findForward("editJuryElement");
    }

    public ActionForward editJuryElementInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
        return mapping.findForward("editJuryElement");
    }

    public ActionForward editJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), EditJuryElement.class, getRenderedObject("thesisJuryElementBean"));

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return editJuryElementInvalid(mapping, actionForm, request, response);
        }

        return redirect(String.format("/phdThesisProcess.do?method=manageThesisJuryElements&processId=%s", getProcess(request)
                .getExternalId()), request);
    }

    public ActionForward deleteJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), DeleteJuryElement.class, getJuryElement(request));
            addSuccessMessage(request, "message.thesis.jury.removed");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
        }

        return redirect(String.format("/phdThesisProcess.do?method=manageThesisJuryElements&processId=%s", getProcess(request)
                .getExternalId()), request);
    }

    private ThesisJuryElement getJuryElement(HttpServletRequest request) {
        return getDomainObject(request, "juryElementId");
    }

    private void swapJuryElements(HttpServletRequest request, ThesisJuryElement e1, ThesisJuryElement e2) {
        try {
            ExecuteProcessActivity.run(getProcess(request), SwapJuryElementsOrder.class,
                    new Pair<ThesisJuryElement, ThesisJuryElement>(e1, e2));
            addSuccessMessage(request, "message.thesis.jury.element.swapped");
        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            throw e;
        }
    }

    private void moveElement(HttpServletRequest request, ThesisJuryElement element, Integer position) {
        try {
            ExecuteProcessActivity.run(getProcess(request), MoveJuryElementOrder.class, new Pair<ThesisJuryElement, Integer>(
                    element, position));
            addSuccessMessage(request, "message.thesis.jury.element.swapped");
        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
        }
    }

    public ActionForward moveUp(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcess process = getProcess(request);
        final ThesisJuryElement juryElement = getJuryElement(request);
        final ThesisJuryElement lower = process.getOrderedThesisJuryElements().lower(juryElement);

        if (lower != null) {
            swapJuryElements(request, juryElement, lower);
        }

        return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward moveDown(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcess process = getProcess(request);
        final ThesisJuryElement juryElement = getJuryElement(request);
        final ThesisJuryElement higher = process.getOrderedThesisJuryElements().higher(juryElement);

        if (higher != null) {
            swapJuryElements(request, juryElement, higher);
        }

        return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward moveTop(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcess process = getProcess(request);
        final ThesisJuryElement juryElement = getJuryElement(request);
        final ThesisJuryElement first = process.getOrderedThesisJuryElements().first();

        if (juryElement != first) {
            moveElement(request, juryElement, first.getElementOrder() - 1);
        }

        return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward moveBottom(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcess process = getProcess(request);
        final ThesisJuryElement juryElement = getJuryElement(request);
        final ThesisJuryElement last = process.getOrderedThesisJuryElements().last();

        if (juryElement != last) {
            moveElement(request, juryElement, last.getElementOrder() - 1);
        }

        return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward prepareAddPresidentJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisJuryElementBean", new PhdThesisJuryElementBean(getProcess(request)));
        return mapping.findForward("addPresidentJuryElement");
    }

    public ActionForward prepareAddPresidentJuryElementInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
        return mapping.findForward("addPresidentJuryElement");
    }

    public ActionForward prepareAddPresidentJuryElementPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
        RenderUtils.invalidateViewState();
        return mapping.findForward("addPresidentJuryElement");
    }

    public ActionForward addPresidentJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), AddPresidentJuryElement.class,
                    getRenderedObject("thesisJuryElementBean"));
            addSuccessMessage(request, "message.thesis.added.jury.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return prepareAddPresidentJuryElementInvalid(mapping, actionForm, request, response);
        }

        return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward prepareValidateJury(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        bean.setWhenJuryValidated(new LocalDate());

        request.setAttribute("thesisBean", bean);
        return mapping.findForward("validateJury");
    }

    public ActionForward prepareValidateJuryInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisBean", getRenderedObject("thesisBean"));
        return mapping.findForward("validateJury");
    }

    public ActionForward validateJury(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), ValidateJury.class, getRenderedObject("thesisBean"));

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return prepareValidateJuryInvalid(mapping, actionForm, request, response);
        }

        return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward printJuryElementsDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final PhdThesisJuryElementsDocument report = new PhdThesisJuryElementsDocument(getProcess(request));

        writeFile(response, report.getReportFileName() + ".pdf", "application/pdf", DefaultDocumentGenerator
                .getGenerator().generateReport(Collections.singletonList(report)));

        return null;
    }

    public ActionForward prepareRejectJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisBean", new PhdThesisProcessBean());
        return mapping.findForward("rejectJuryElements");
    }

    public ActionForward rejectJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request), RejectJuryElements.class, getRenderedObject("thesisBean"));

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("thesisBean", getRenderedObject("thesisBean"));
            return mapping.findForward("rejectJuryElements");
        }

        return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    // end thesis jury elements management

    // Request Jury Reviews
    public ActionForward prepareRequestJuryReviews(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("requestJuryReviewsBean", new PhdThesisProcessBean());

        return mapping.findForward("requestJuryReviews");
    }

    public ActionForward prepareRequestJuryReviewsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("requestJuryReviewsBean", getRenderedObject("requestJuryReviewsBean"));

        return mapping.findForward("requestJuryReviews");
    }

    public ActionForward requestJuryReviews(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            ExecuteProcessActivity
                    .run(getProcess(request), RequestJuryReviews.class, getRenderedObject("requestJuryReviewsBean"));
        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return prepareRequestJuryReviewsInvalid(mapping, form, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    // End of Request Jury Reviews

    // Remind Jury Reviews
    public ActionForward prepareRemindJuryReviews(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("remindJuryReviewsBean", new PhdThesisProcessBean());

        return mapping.findForward("remindJuryReviews");
    }

    public ActionForward prepareRemindJuryReviewsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("remindJuryReviewsBean", getRenderedObject("remindJuryReviewsBean"));

        return mapping.findForward("remindJuryReviews");
    }

    public ActionForward remindJuryReviews(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            ExecuteProcessActivity.run(getProcess(request), RemindJuryReviewToReporters.class,
                    getRenderedObject("remindJuryReviewsBean"));
        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return prepareRemindJuryReviewsInvalid(mapping, form, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    // end remind jury reviews

    // Schedule thesis discussion

    public ActionForward prepareScheduleThesisDiscussion(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        final PhdThesisProcess thesisProcess = getProcess(request);

        bean.setThesisProcess(thesisProcess);
        setDefaultDiscussionMailInformation(bean, thesisProcess);

        request.setAttribute("thesisProcessBean", bean);
        return mapping.findForward("scheduleThesisDiscussion");
    }

    private void setDefaultDiscussionMailInformation(final PhdThesisProcessBean bean, final PhdThesisProcess thesisProcess) {
        final PhdIndividualProgramProcess process = thesisProcess.getIndividualProgramProcess();
        bean.setMailSubject(AlertService.getSubjectPrefixed(process,
                "message.phd.thesis.schedule.thesis.discussion.default.subject"));
        bean.setMailBody(AlertService.getBodyText(process, "message.phd.thesis.schedule.thesis.discussion.default.body"));
    }

    public ActionForward scheduleThesisDiscussionInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        return mapping.findForward("scheduleThesisDiscussion");
    }

    public ActionForward scheduleThesisDiscussionPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("scheduleThesisDiscussion");
    }

    public ActionForward scheduleThesisDiscussion(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcess thesisProcess = getProcess(request);

        try {
            ExecuteProcessActivity.run(thesisProcess, ScheduleThesisDiscussion.class, getThesisProcessBean());

        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return scheduleThesisDiscussionPostback(mapping, actionForm, request, response);
        }

        return viewIndividualProgramProcess(request, thesisProcess);
    }

    // End of schedule thesis discussion

    // Submit thesis
    public ActionForward prepareSubmitThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.FINAL_THESIS));

        request.setAttribute("submitThesisBean", bean);

        return mapping.findForward("submitThesis");
    }

    public ActionForward prepareSubmitThesisInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("submitThesisBean", getRenderedObject("submitThesisBean"));
        RenderUtils.invalidateViewState("submitThesisBean.edit.documents");

        return mapping.findForward("submitThesis");
    }

    public ActionForward submitThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {

            if (!RenderUtils.getViewState("submitThesisBean.edit.documents").isValid()) {
                addErrorMessage(request, "error.phd.invalid.documents");
                return prepareSubmitThesisInvalid(mapping, form, request, response);
            }

            ExecuteProcessActivity.run(getProcess(request), SubmitThesis.class, getRenderedObject("submitThesisBean"));

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return prepareSubmitThesisInvalid(mapping, form, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));

    }

    // End of submit thesis

    // Ratify final thesis
    public ActionForward prepareRatifyFinalThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.FINAL_THESIS_RATIFICATION_DOCUMENT));

        request.setAttribute("thesisProcessBean", bean);
        return mapping.findForward("ratifyFinalThesis");
    }

    public ActionForward ratifyFinalThesisInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        return mapping.findForward("ratifyFinalThesis");
    }

    public ActionForward ratifyFinalThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {

            if (!RenderUtils.getViewState("thesisProcessBean.edit").isValid()) {
                return ratifyFinalThesisInvalid(mapping, actionForm, request, response);
            }

            if (!RenderUtils.getViewState("thesisProcessBean.edit.documents").isValid()) {
                addErrorMessage(request, "error.phd.invalid.documents");
                return ratifyFinalThesisInvalid(mapping, actionForm, request, response);
            }

            ExecuteProcessActivity.run(getProcess(request), RatifyFinalThesis.class, getThesisProcessBean());

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return ratifyFinalThesisInvalid(mapping, actionForm, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));

    }

    // End of ratify final thesis

    // Set final grade

    public ActionForward prepareSetFinalGrade(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.CONCLUSION_DOCUMENT));

        request.setAttribute("thesisProcessBean", bean);
        return mapping.findForward("setFinalGrade");
    }

    public ActionForward setFinalGradeInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        return mapping.findForward("setFinalGrade");
    }

    public ActionForward setFinalGrade(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {

            if (!RenderUtils.getViewState("thesisProcessBean.edit").isValid()) {
                return setFinalGradeInvalid(mapping, actionForm, request, response);
            }

            if (!RenderUtils.getViewState("thesisProcessBean.edit.documents").isValid()) {
                addErrorMessage(request, "error.phd.invalid.documents");
                return setFinalGradeInvalid(mapping, actionForm, request, response);
            }

            ExecuteProcessActivity.run(getProcess(request), SetFinalGrade.class, getThesisProcessBean());

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return setFinalGradeInvalid(mapping, actionForm, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    // End of set final grade

    // Reject jury elements documents

    public ActionForward prepareRejectJuryElementsDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        request.setAttribute("thesisProcessBean", bean);

        return mapping.findForward("rejectJuryElementsDocuments");
    }

    public ActionForward rejectJuryElementsDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        try {

            ExecuteProcessActivity.run(getProcess(request), RejectJuryElementsDocuments.class,
                    getRenderedObject("thesisProcessBean"));
            addSuccessMessage(request, "message.thesis.jury.elements.documents.rejected.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("thesisProcessBean", getRenderedObject("thesisProcessBean"));
            return mapping.findForward("rejectJuryElementsDocuments");
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    // End of reject jury elements document

    // Manage phd thesis process meetings
    public ActionForward viewMeetingSchedulingProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdThesisProcessBean bean = new PhdThesisProcessBean();
        request.setAttribute("thesisProcessBean", bean);

        return mapping.findForward("viewMeetingSchedulingProcess");
    }

    // End of manage phd thesis process meetings

    public ActionForward prepareReplaceDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramDocumentUploadBean bean =
                new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.valueOf(request.getParameter("type")));

        request.setAttribute("documentBean", bean);

        if (bean.getType() == PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK) {
            bean.setJuryElement(getJuryElement(request));
        }

        return mapping.findForward("replaceDocument");
    }

    public ActionForward replaceDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {

            final IViewState viewState = RenderUtils.getViewState("documentBean");
            if (!viewState.isValid()) {
                return juryReportFeedbackUploadInvalid(mapping, form, request, response);
            }

            ExecuteProcessActivity.run(getProcess(request), ReplaceDocument.class, getRenderedObject("documentBean"));
            addSuccessMessage(request, "message.replace.document.done.with.success");
        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return replaceDocumentInvalid(mapping, form, request, response);
        }

        return manageThesisDocuments(mapping, form, request, response);
    }

    public ActionForward replaceDocumentInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        return mapping.findForward("replaceDocument");
    }

    // jury report feedback operations

    @Override
    public ActionForward prepareJuryReportFeedbackUpload(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean(getProcess(request).getIndividualProgramProcess());
        bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK));

        request.setAttribute("thesisProcessBean", bean);
        request.setAttribute("thesisDocuments", getProcess(request).getThesisDocumentsToFeedback());

        return mapping.findForward("juryReporterFeedbackUpload");
    }

    public ActionForward juryReportFeedbackUploadPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("thesisProcessBean", getThesisProcessBean());
        request.setAttribute("thesisDocuments", getProcess(request).getThesisDocumentsToFeedback());
        return mapping.findForward("juryReporterFeedbackUpload");
    }

    public ActionForward manageStates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdThesisProcessBean bean = new PhdThesisProcessBean(getProcess(request).getIndividualProgramProcess());
        request.setAttribute("thesisProcessBean", bean);

        return mapping.findForward("manageStates");
    }

    public ActionForward addState(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            PhdThesisProcessBean bean = getRenderedObject("thesisProcessBean");
            ExecuteProcessActivity.run(getProcess(request), AddState.class, bean);
        } catch (PhdDomainOperationException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
        }

        return manageStates(mapping, form, request, response);
    }

    public ActionForward addStateInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdThesisProcessBean bean = getRenderedObject("thesisProcessBean");

        request.setAttribute("thesisProcessBean", bean);

        return mapping.findForward("manageStates");
    }

    public ActionForward removeLastState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            ExecuteProcessActivity.run(getProcess(request), RemoveLastState.class, null);
        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
        }
        return manageStates(mapping, form, request, response);
    }

    // Phd Thesis process information edit
    public ActionForward prepareEditPhdThesisProcessInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("phdThesisProcessBean", new PhdThesisProcessBean(getProcess(request).getIndividualProgramProcess()));
        return mapping.findForward("editPhdThesisProcessInformation");
    }

    public ActionForward editPhdThesisProcessInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("phdThesisProcessBean", getRenderedObject("phdThesisProcessBean"));
        return mapping.findForward("editPhdThesisProcessInformation");
    }

    public ActionForward editPhdThesisProcessInformationPostback(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("phdThesisProcessBean", getRenderedObject("phdThesisProcessBean"));
        RenderUtils.invalidateViewState();
        return mapping.findForward("editPhdThesisProcessInformation");
    }

    public ActionForward editPhdThesisProcessInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdThesisProcessBean bean = getRenderedObject("phdThesisProcessBean");
        request.setAttribute("phdThesisProcessBean", bean);

        try {
            ExecuteProcessActivity.run(getProcess(request), EditPhdThesisProcessInformation.class.getSimpleName(), bean);
            addSuccessMessage(request, "message.phdThesisProcessInformation.edit.success");
            return viewIndividualProgramProcess(request, getProcess(request));

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("editPhdThesisProcessInformation");
        }
    }

    // End of Phd Thesis process information edit

    /* Conclusion Process */

    public ActionForward listConclusionProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("listConclusionProcess");
    }

    public ActionForward prepareCreateConclusionProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdIndividualProgramProcess individualProgramProcess = getProcess(request).getIndividualProgramProcess();
        PhdConclusionProcessBean bean = new PhdConclusionProcessBean(individualProgramProcess);

        LocalDate conclusionDate = null;
        if (!individualProgramProcess.getStudyPlan().isExempted()) {
            //TODO: phd-refactor this should be change to terminal program conclusion
            conclusionDate =
                    individualProgramProcess.getRegistration().getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE)
                            .getConclusionDate().toDateMidnight().toLocalDate();
        } else {
            conclusionDate = bean.getConclusionDate();
        }

        PhdProgramInformation phdProgramInformation =
                getProcess(request).getIndividualProgramProcess().getPhdProgram().getPhdProgramInformationByDate(conclusionDate);

        request.setAttribute("isExempted", individualProgramProcess.getStudyPlan().isExempted());
        request.setAttribute("conclusionDateForPhdInformation", conclusionDate);
        request.setAttribute("phdProgramInformation", phdProgramInformation);
        request.setAttribute("phdConclusionProcessBean", bean);

        return mapping.findForward("createConclusionProcess");
    }

    public ActionForward createConclusionProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdConclusionProcessBean bean = getRenderedObject("phdConclusionProcessBean");
        try {
            ExecuteProcessActivity.run(getProcess(request), ConcludePhdProcess.class, bean);
        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return createConclusionProcessInvalid(mapping, form, request, response);
        }

        return listConclusionProcesses(mapping, form, request, response);
    }

    public ActionForward createConclusionProcessInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdConclusionProcessBean bean = getRenderedObject("phdConclusionProcessBean");
        PhdIndividualProgramProcess individualProgramProcess = getProcess(request).getIndividualProgramProcess();

        LocalDate conclusionDate = null;
        if (!individualProgramProcess.getStudyPlan().isExempted()) {
            //TODO: phd-refactor this should be change to terminal program conclusion
            conclusionDate =
                    individualProgramProcess.getRegistration().getLastStudentCurricularPlan().getCycle(CycleType.THIRD_CYCLE)
                            .getConclusionDate().toDateMidnight().toLocalDate();
        } else {
            conclusionDate = bean.getConclusionDate();
        }

        PhdProgramInformation phdProgramInformation =
                getProcess(request).getIndividualProgramProcess().getPhdProgram().getPhdProgramInformationByDate(conclusionDate);

        request.setAttribute("isExempted", individualProgramProcess.getStudyPlan().isExempted());
        request.setAttribute("conclusionDateForPhdInformation", conclusionDate);
        request.setAttribute("phdProgramInformation", phdProgramInformation);
        request.setAttribute("phdConclusionProcessBean", bean);

        return mapping.findForward("createConclusionProcess");
    }

    public ActionForward setPhdJuryElementsRatificationEntity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdThesisProcessBean bean = getRenderedObject("thesisProcessBean");
        final PhdThesisProcess process = getProcess(request);

        ExecuteProcessActivity.run(process, SetPhdJuryElementRatificationEntity.class, bean);

        return prepareSubmitJuryElementsDocument(mapping, form, request, response, bean);
    }

    public ActionForward setPhdJuryElementsRatificationEntityPostback(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final PhdThesisProcessBean bean = getRenderedObject("thesisProcessBean");
        RenderUtils.invalidateViewState();
        return prepareSubmitJuryElementsDocument(mapping, form, request, response, bean);
    }

    public ActionForward setPhdJuryElementsRatificationEntityInvalid(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final PhdThesisProcessBean bean = getRenderedObject("thesisProcessBean");
        return prepareSubmitJuryElementsDocument(mapping, form, request, response, bean);
    }

    /* EDIT PHD STATES */

    public ActionForward prepareEditState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessState state = getDomainObject(request, "stateId");
        PhdProcessStateBean bean = new PhdProcessStateBean(state);

        request.setAttribute("bean", bean);

        return mapping.findForward("editPhdProcessState");
    }

    public ActionForward editState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessStateBean bean = getRenderedObject("bean");
        bean.getState().editStateDate(bean);

        return manageStates(mapping, form, request, response);
    }

    public ActionForward editStateInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProcessStateBean bean = getRenderedObject("bean");
        request.setAttribute("bean", bean);

        return mapping.findForward("editPhdProcessState");
    }

    /* EDIT PHD STATES */

    public ActionForward viewLogs(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        return mapping.findForward("viewLogs");
    }

    @SuppressWarnings("unchecked")
    protected List<PhdProgramDocumentUploadBean> getDocumentsToUpload() {
        return (List<PhdProgramDocumentUploadBean>) getObjectFromViewState("documentsToUpload");
    }

    protected boolean hasAnyDocumentToUpload() {
        for (final PhdProgramDocumentUploadBean each : getDocumentsToUpload()) {
            if (each.hasAnyInformation()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ActionForward deleteDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        prepareDocumentsToUpload(request);
        return executeActivity(org.fenixedu.academic.domain.phd.thesis.activities.DeleteDocument.class,
                getDomainObject(request, "documentId"), request, mapping, "manageThesisDocuments", "manageThesisDocuments",
                "message.document.deleted.successfuly");
    }

    @Override
    public ActionForward manageThesisDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        prepareDocumentsToUpload(request);

        return mapping.findForward("manageThesisDocuments");
    }

    private void prepareDocumentsToUpload(HttpServletRequest request) {
        request.setAttribute("documentsToUpload", Arrays.asList(new PhdProgramDocumentUploadBean(),
                new PhdProgramDocumentUploadBean(), new PhdProgramDocumentUploadBean()));
    }

    public ActionForward uploadDocumentsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("documentsToUpload", getDocumentsToUpload());
        return mapping.findForward("manageThesisDocuments");
    }

    public ActionForward uploadDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        if (!hasAnyDocumentToUpload()) {
            request.setAttribute("documentsToUpload", getDocumentsToUpload());

            addErrorMessage(request, "message.no.documents.to.upload");

            return mapping.findForward("manageThesisDocuments");
        }

        final ActionForward result =
                executeActivity(org.fenixedu.academic.domain.phd.thesis.activities.UploadDocuments.class, getDocumentsToUpload(),
                        request, mapping, "manageThesisDocuments", "manageThesisDocuments",
                        "message.documents.uploaded.with.success");

        RenderUtils.invalidateViewState("documentsToUpload");

        prepareDocumentsToUpload(request);

        return result;

    }

}
