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
package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.UploadCandidacyFeedback;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdDocumentsZip;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

abstract public class CommonPhdCandidacyDA extends PhdProcessDA {

    @Override
    protected PhdProgramCandidacyProcess getProcess(HttpServletRequest request) {
        return (PhdProgramCandidacyProcess) super.getProcess(request);
    }

    public ActionForward viewIndividualProgramProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return viewIndividualProgramProcess(request, getProcess(request));
    }

    protected ActionForward viewIndividualProgramProcess(HttpServletRequest request, final PhdProgramCandidacyProcess process) {
        return redirect(String.format("/phdIndividualProgramProcess.do?method=viewProcess&processId=%s", process
                .getIndividualProgramProcess().getExternalId()), request);
    }

    public ActionForward manageCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("manageCandidacyDocuments");
    }

    public ActionForward manageCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramDocumentUploadBean bean = new PhdProgramDocumentUploadBean();
        bean.setType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW);

        final PhdProgramCandidacyProcessStateBean stateBean =
                new PhdProgramCandidacyProcessStateBean(getProcess(request).getIndividualProgramProcess());
        stateBean.setState(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION);

        request.setAttribute("documentToUpload", bean);
        request.setAttribute("stateBean", stateBean);

        return mapping.findForward("manageCandidacyReview");
    }

    public ActionForward manageCandidacyReviewPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramDocumentUploadBean bean = new PhdProgramDocumentUploadBean();
        bean.setType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW);

        request.setAttribute("documentToUpload", bean);
        request.setAttribute("stateBean", getRenderedObject("stateBean"));

        RenderUtils.invalidateViewState();

        return mapping.findForward("manageCandidacyReview");
    }

    public ActionForward uploadCandidacyReviewInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("documentToUpload", getRenderedObject("documentToUpload"));
        request.setAttribute("stateBean", getRenderedObject("stateBean"));
        RenderUtils.invalidateViewState();
        return mapping.findForward("manageCandidacyReview");
    }

    public ActionForward uploadCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramDocumentUploadBean bean = getRenderedObject("documentToUpload");

        if (!bean.hasAnyInformation()) {
            return uploadCandidacyReviewInvalid(mapping, actionForm, request, response);
        }

        try {
            ExecuteProcessActivity.run(getProcess(request),
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.UploadCandidacyReview.class,
                    Collections.singletonList(bean));
            addSuccessMessage(request, "message.document.uploaded.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            bean.setFile(null);
            return uploadCandidacyReviewInvalid(mapping, actionForm, request, response);
        }

        RenderUtils.invalidateViewState();
        return manageCandidacyReview(mapping, actionForm, request, response);
    }

    public ActionForward prepareRejectCandidacyProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdProgramCandidacyProcessStateBean stateBean =
                new PhdProgramCandidacyProcessStateBean(getProcess(request).getIndividualProgramProcess());
        request.setAttribute("stateBean", stateBean);
        return mapping.findForward("rejectCandidacyProcess");
    }

    public ActionForward prepareRejectCandidacyProcessPostback(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdProgramCandidacyProcessStateBean bean = getRenderedObject("stateBean");
        request.setAttribute("stateBean", bean);

        return mapping.findForward("rejectCandidacyProcess");
    }

    public ActionForward rejectCandidacyProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            final PhdProgramCandidacyProcessStateBean bean = getRenderedObject("stateBean");
            bean.setState(PhdProgramCandidacyProcessState.REJECTED);
            ExecuteProcessActivity.run(getProcess(request),
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.RejectCandidacyProcess.class, bean);
        } catch (final DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return rejectCandidacyProcessInvalid(mapping, actionForm, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward rejectCandidacyProcessInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final PhdProgramCandidacyProcessStateBean bean = getRenderedObject("stateBean");
        request.setAttribute("stateBean", bean);

        return mapping.findForward("rejectCandidacyProcess");
    }

    public ActionForward requestRatifyCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            final PhdProgramCandidacyProcess process = getProcess(request);
            ExecuteProcessActivity.run(process,
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.RequestRatifyCandidacy.class,
                    getRenderedObject("stateBean"));
            return viewIndividualProgramProcess(request, process);

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return uploadCandidacyReviewInvalid(mapping, actionForm, request, response);
        }
    }

    public ActionForward deleteCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            ExecuteProcessActivity.run(getProcess(request),
                    net.sourceforge.fenixedu.domain.phd.candidacy.activities.DeleteCandidacyReview.class, getDocument(request));
            addSuccessMessage(request, "message.document.deleted.successfuly");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        return manageCandidacyReview(mapping, actionForm, request, response);
    }

    protected PhdProgramProcessDocument getDocument(HttpServletRequest request) {
        return getDomainObject(request, "documentId");
    }

    public ActionForward downloadCandidacyDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        writeFile(response, getCandidacyDocumentsFilename(request), PhdDocumentsZip.ZIP_MIME_TYPE, createZip(request));
        return null;
    }

    private String getCandidacyDocumentsFilename(HttpServletRequest request) {
        final PhdIndividualProgramProcess process = getProcess(request).getIndividualProgramProcess();
        return String.format("%s-%s.zip", process.getProcessNumber().replace("/", "-"),
                getMessageFromResource("label.phd.manageCandidacyDocuments").replace(" ", "_"));
    }

    /*
     * Upload candidacy feedback
     */
    public ActionForward prepareUploadCandidacyFeedback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = getLoggedPerson(request);
        final PhdCandidacyFeedbackRequestProcess feedbackRequest = getProcess(request).getFeedbackRequest();

        final PhdProgramDocumentUploadBean bean = new PhdProgramDocumentUploadBean();
        bean.setType(PhdIndividualProgramDocumentType.CANDIDACY_FEEDBACK_DOCUMENT);

        request.setAttribute("documentBean", bean);
        request.setAttribute("canUploadDocuments", feedbackRequest.canUploadDocuments());
        request.setAttribute("sharedDocuments", feedbackRequest.getSharedDocumentsContent());
        request.setAttribute("lastFeedbackDocument", feedbackRequest.getElement(person).getLastFeedbackDocument());

        return mapping.findForward("uploadCandidacyFeedback");
    }

    public ActionForward prepareUploadCandidacyFeedbackInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final Person person = getLoggedPerson(request);
        final PhdCandidacyFeedbackRequestProcess feedbackRequest = getProcess(request).getFeedbackRequest();

        request.setAttribute("documentBean", getRenderedObject("documentBean"));
        request.setAttribute("canUploadDocuments", feedbackRequest.canUploadDocuments());
        request.setAttribute("sharedDocuments", feedbackRequest.getSharedDocumentsContent());
        request.setAttribute("lastFeedbackDocument", feedbackRequest.getElement(person).getLastFeedbackDocument());

        return mapping.findForward("uploadCandidacyFeedback");
    }

    public ActionForward uploadCandidacyFeedback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {

            ExecuteProcessActivity.run(getProcess(request).getFeedbackRequest(), UploadCandidacyFeedback.class,
                    getRenderedObject("documentBean"));

            addSuccessMessage(request, "message.phd.candidacy.feedback.document.uploaded.with.success");

        } catch (final DomainException e) {
            addErrorMessage(request, e.getMessage(), e.getArgs());
            return prepareUploadCandidacyFeedbackInvalid(mapping, actionForm, request, response);
        }

        return viewIndividualProgramProcess(request, getProcess(request));
    }

    /*
     * End of upload candidacy feedback
     */

    /*
     * Download candidacy feedback request shared documents
     */

    public ActionForward candidacyFeedbackDocumentsDownload(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        final PhdProgramCandidacyProcess process = getProcess(request);
        final Set<PhdProgramProcessDocument> documents = process.getFeedbackRequest().getSharedDocumentsContent();

        if (!documents.isEmpty()) {
            writeFile(response, getZipDocumentsFilename(process.getIndividualProgramProcess()), PhdDocumentsZip.ZIP_MIME_TYPE,
                    createZip(documents));
            return null;
        }

        return prepareUploadCandidacyFeedbackInvalid(mapping, actionForm, request, response);
    }

}
