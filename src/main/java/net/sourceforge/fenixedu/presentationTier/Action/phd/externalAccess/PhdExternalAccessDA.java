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
package net.sourceforge.fenixedu.presentationTier.Action.phd.externalAccess;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElement;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.DownloadCandidacyFeedbackDocuments;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.ExternalUploadCandidacyFeedback;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryDocumentsDownload;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReporterFeedbackExternalUpload;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReviewDocumentsDownload;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdDocumentsZip;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * Serves as entry point to external phd programs access. To add new operations
 * define new types in PhdProcessAccessType enum and define methods here in the
 * following format:
 * 
 * <pre>
 * - method1: 'prepare<Descriptor>'
 * - method2: '<descriptor>'
 * - method3: 'prepare<Descriptor>Invalid'
 * </pre>
 * 
 * Each new method will handle an operation in this page
 */

@Mapping(path = "/phdExternalAccess", module = "publico")
@Forwards(tileProperties = @Tile(extend = "definition.phd.external.access"), value = {

@Forward(name = "showOperations", path = "/phd/externalAccess/showOperations.jsp"),

@Forward(name = "juryDocumentsDownload", path = "/phd/externalAccess/downloadDocuments.jsp"),

@Forward(name = "juryReporterFeedbackUpload", path = "/phd/externalAccess/thesis/juryReporterFeedbackUpload.jsp"),

@Forward(name = "candidacyFeedbackDocumentsDownload", path = "/phd/externalAccess/downloadDocuments.jsp"),

@Forward(name = "candidacyFeedbackUpload", path = "/phd/externalAccess/candidacy/candidacyFeedbackUpload.jsp"),

@Forward(name = "juryReviewDocumentsDownload", path = "/phd/externalAccess/thesis/juryReviewDocumentsDownload.jsp")

})
public class PhdExternalAccessDA extends PhdProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("dont-cache-pages-in-search-engines", Boolean.TRUE);
        request.setAttribute("participant", getPhdParticipant(request));

        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("showOperations");
    }

    private PhdParticipant getPhdParticipant(HttpServletRequest request) {
        final PhdExternalOperationBean bean = getOperationBean();
        return bean != null ? bean.getParticipant() : PhdParticipant.readByAccessHashCode(getHash(request));
    }

    private PhdExternalOperationBean getOperationBean() {
        return getRenderedObject("operationBean");
    }

    private String getHash(HttpServletRequest request) {
        return (String) getFromRequest(request, "hash");
    }

    // jury document download

    public ActionForward prepareJuryDocumentsDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("operationBean", new PhdExternalOperationBean(getPhdParticipant(request),
                PhdProcessAccessType.JURY_DOCUMENTS_DOWNLOAD));

        return mapping.findForward("juryDocumentsDownload");
    }

    public ActionForward prepareJuryDocumentsDownloadInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("operationBean", getOperationBean());

        return mapping.findForward("juryDocumentsDownload");
    }

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
        return getPhdParticipant(request).getIndividualProcess();
    }

    public ActionForward juryDocumentsDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {

            final PhdIndividualProgramProcess process = getProcess(request);
            ExecuteProcessActivity.run(process.getThesisProcess(), JuryDocumentsDownload.class, getOperationBean());
            writeFile(response, getZipDocumentsFilename(process), PhdDocumentsZip.ZIP_MIME_TYPE, createZip(process
                    .getThesisProcess().getThesisDocumentsToFeedback()));

            return null;

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return prepareJuryDocumentsDownloadInvalid(mapping, form, request, response);
        }

    }

    // end jury document download

    // jury report feedback operations

    public ActionForward prepareJuryReporterFeedbackUpload(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdExternalOperationBean bean =
                new PhdExternalOperationBean(getPhdParticipant(request), PhdProcessAccessType.JURY_REPORTER_FEEDBACK_UPLOAD);

        bean.setDocumentBean(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK));

        request.setAttribute("operationBean", bean);
        request.setAttribute("lastReportFeedbackDocument", getThesisJuryElement(request, bean).getLastFeedbackDocument());
        request.setAttribute("waitingForJuryReporterFeedback", getProcess(request).getThesisProcess()
                .isWaitingForJuryReporterFeedback());

        return mapping.findForward("juryReporterFeedbackUpload");
    }

    public ActionForward prepareJuryReporterFeedbackUploadInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdExternalOperationBean bean = getOperationBean();
        request.setAttribute("operationBean", bean);
        request.setAttribute("lastReportFeedbackDocument", getThesisJuryElement(request, bean).getLastFeedbackDocument());
        request.setAttribute("waitingForJuryReporterFeedback", getProcess(request).getThesisProcess()
                .isWaitingForJuryReporterFeedback());

        return mapping.findForward("juryReporterFeedbackUpload");
    }

    private ThesisJuryElement getThesisJuryElement(HttpServletRequest request, final PhdExternalOperationBean bean) {
        return bean.getParticipant().getThesisJuryElement(getProcess(request).getThesisProcess());
    }

    public ActionForward juryReporterFeedbackUpload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {

            if (!RenderUtils.getViewState("operationBean").isValid()) {
                return prepareJuryReporterFeedbackUploadInvalid(mapping, actionForm, request, response);
            }

            ExecuteProcessActivity.run(getProcess(request).getThesisProcess(), JuryReporterFeedbackExternalUpload.class,
                    getOperationBean());

            addSuccessMessage(request, "message.jury.report.feedback.upload.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return prepareJuryReporterFeedbackUploadInvalid(mapping, actionForm, request, response);
        }

        return prepare(mapping, actionForm, request, response);
    }

    // end of jury report feedback operations

    // Download candidacy feedback documents

    public ActionForward prepareCandidacyFeedbackDocumentsDownload(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("operationBean", new PhdExternalOperationBean(getPhdParticipant(request),
                PhdProcessAccessType.CANDIDACY_FEEDBACK_DOCUMENTS_DOWNLOAD));
        return mapping.findForward("candidacyFeedbackDocumentsDownload");
    }

    public ActionForward prepareCandidacyFeedbackDocumentsDownloadInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("operationBean", getOperationBean());
        return mapping.findForward("candidacyFeedbackDocumentsDownload");
    }

    public ActionForward candidacyFeedbackDocumentsDownload(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {

            final PhdCandidacyFeedbackRequestProcess process = getFeedBackRequest(request);
            ExecuteProcessActivity.run(process, DownloadCandidacyFeedbackDocuments.class, getOperationBean());

            final Set<PhdProgramProcessDocument> documents = process.getSharedDocumentsContent();

            if (!documents.isEmpty()) {
                writeFile(response, getZipDocumentsFilename(process.getCandidacyProcess().getIndividualProgramProcess()),
                        PhdDocumentsZip.ZIP_MIME_TYPE, createZip(documents));

                return null;

            } else {
                addErrorMessage(request, "error.phd.candidacy.feedback.request.no.documents.to.download");
            }

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
        }

        return prepareJuryDocumentsDownloadInvalid(mapping, actionForm, request, response);
    }

    private PhdCandidacyFeedbackRequestProcess getFeedBackRequest(HttpServletRequest request) {
        return getProcess(request).getCandidacyProcess().getFeedbackRequest();
    }

    public ActionForward prepareCandidacyFeedbackUpload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final PhdExternalOperationBean bean =
                new PhdExternalOperationBean(getPhdParticipant(request), PhdProcessAccessType.CANDIDACY_FEEDBACK_UPLOAD);

        bean.setDocumentBean(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.CANDIDACY_FEEDBACK_DOCUMENT));

        request.setAttribute("operationBean", bean);
        request.setAttribute("canUploadDocuments", getFeedBackRequest(request).canUploadDocuments());
        request.setAttribute("lastFeedbackDocument", getCandidacyFeedbackRequestElement(request, bean).getLastFeedbackDocument());

        return mapping.findForward("candidacyFeedbackUpload");
    }

    private PhdCandidacyFeedbackRequestElement getCandidacyFeedbackRequestElement(HttpServletRequest request,
            PhdExternalOperationBean bean) {
        return bean.getParticipant().getPhdCandidacyFeedbackRequestElement(getFeedBackRequest(request));
    }

    public ActionForward prepareCandidacyFeedbackUploadInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final PhdExternalOperationBean bean = getOperationBean();
        request.setAttribute("operationBean", bean);
        request.setAttribute("canUploadDocuments", getFeedBackRequest(request).canUploadDocuments());
        request.setAttribute("lastFeedbackDocument", getCandidacyFeedbackRequestElement(request, bean).getLastFeedbackDocument());

        return mapping.findForward("candidacyFeedbackUpload");
    }

    public ActionForward candidacyFeedbackUpload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        try {

            if (!RenderUtils.getViewState("operationBean").isValid()) {
                return prepareCandidacyFeedbackUploadInvalid(mapping, actionForm, request, response);
            }

            ExecuteProcessActivity.run(getFeedBackRequest(request), ExternalUploadCandidacyFeedback.class, getOperationBean());

            addSuccessMessage(request, "message.phd.candidacy.feedback.document.uploaded.with.success");

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return prepareCandidacyFeedbackUploadInvalid(mapping, actionForm, request, response);
        }

        return prepare(mapping, actionForm, request, response);
    }

    // end of Download candidacy feedback documents

    // Jury review documents download

    public ActionForward prepareJuryReviewDocumentsDownload(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("operationBean", new PhdExternalOperationBean(getPhdParticipant(request),
                PhdProcessAccessType.JURY_REVIEW_DOCUMENTS_DOWNLOAD));

        return mapping.findForward("juryReviewDocumentsDownload");
    }

    public ActionForward prepareJuryReviewDocumentsDownloadInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("operationBean", getOperationBean());
        return mapping.findForward("juryReviewDocumentsDownload");
    }

    public ActionForward juryReviewDocumentsDownload(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {

            final PhdIndividualProgramProcess process = getProcess(request);
            ExecuteProcessActivity.run(process.getThesisProcess(), JuryReviewDocumentsDownload.class, getOperationBean());

            writeFile(response, getZipDocumentsFilename(process), PhdDocumentsZip.ZIP_MIME_TYPE, createZip(process
                    .getThesisProcess().getReportThesisJuryElementDocuments()));

            return null;

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return prepareJuryReviewDocumentsDownloadInvalid(mapping, actionForm, request, response);
        }
    }

    // End of jury review documents download
}
