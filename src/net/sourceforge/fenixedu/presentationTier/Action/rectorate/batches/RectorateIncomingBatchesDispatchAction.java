package net.sourceforge.fenixedu.presentationTier.Action.rectorate.batches;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionState;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.documentRequestExcel.DocumentRequestExcelUtils;
import net.sourceforge.fenixedu.presentationTier.Action.commons.zip.ZipUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/rectorateIncomingBatches", module = "rectorate")
@Forwards({ @Forward(name = "index", path = "/rectorate/incomingBatches.jsp"),
        @Forward(name = "viewBatch", path = "/rectorate/showBatch.jsp") })
public class RectorateIncomingBatchesDispatchAction extends FenixDispatchAction {

    public ActionForward index(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Set<RectorateSubmissionBatch> batches = new HashSet<RectorateSubmissionBatch>();
        for (RectorateSubmissionBatch batch : RectorateSubmissionBatch.getRectorateSubmissionBatchesByState(
                rootDomainObject.getAdministrativeOfficesSet(), RectorateSubmissionState.SENT)) {
            if (!getRelevantDocuments(batch.getDocumentRequestSet()).isEmpty()) {
                batches.add(batch);
            }
        }
        request.setAttribute("batches", batches);
        return mapping.findForward("index");
    }

    private boolean obeysToPresentationRestriction(AcademicServiceRequest docRequest) {
        return !docRequest.isCancelled() && !docRequest.isRejected() && docRequest.isRegistryDiploma()
                || docRequest.isDiplomaSupplement();
    }

    private Set<AcademicServiceRequest> getRelevantDocuments(Set<AcademicServiceRequest> documentRequestSet) {
        Set<AcademicServiceRequest> requests = new HashSet<AcademicServiceRequest>();
        for (AcademicServiceRequest docRequest : documentRequestSet) {
            if (obeysToPresentationRestriction(docRequest)) {
                requests.add(docRequest);
            }
        }
        return requests;
    }

    public ActionForward viewBatch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
        Set<String> actions = new HashSet<String>();

        actions.add("generateMetadataForRegistry");
        actions.add("zipDocuments");

        request.setAttribute("batch", batch);

        Set<AcademicServiceRequest> requests = getRelevantDocuments(batch.getDocumentRequestSet());

        request.setAttribute("requests", requests);
        request.setAttribute("actions", actions);
        return mapping.findForward("viewBatch");
    }

    public ActionForward zipDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
        Set<AcademicServiceRequest> requestsToZip = getRelevantDocuments(batch.getDocumentRequestSet());

        if (!requestsToZip.isEmpty()) {
            ZipUtils zipUtils = new ZipUtils();
            zipUtils.createAndFlushArchive(requestsToZip, response, batch);
            return null;
        }
        addActionMessage(request, "error.rectorateSubmission.noDocumentsToZip");
        request.setAttribute("batchOid", batch.getExternalId());
        return viewBatch(mapping, actionForm, request, response);
    }

    public ActionForward generateMetadataForRegistry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
        Set<AcademicServiceRequest> docs = getRelevantDocuments(batch.getDocumentRequestSet());
        DocumentRequestExcelUtils excelUtils = new DocumentRequestExcelUtils(request, response);
        excelUtils.generateSortedExcel(docs, "registos-");
        return null;
    }

    protected IDocumentRequest getDocumentRequest(HttpServletRequest request) {
        return (IDocumentRequest) rootDomainObject.readAcademicServiceRequestByOID(getRequestParameterAsInteger(request,
                "documentRequestId"));
    }

    public ActionForward printDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final IDocumentRequest documentRequest = getDocumentRequest(request);
        try {
            byte[] data = documentRequest.generateDocument();

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + documentRequest.getReportFileName() + ".pdf");

            final ServletOutputStream writer = response.getOutputStream();
            writer.write(data);
            writer.flush();
            writer.close();

            response.flushBuffer();
            return null;
        } catch (DomainException e) {
            addActionMessage(request, e.getKey());
            return viewBatch(mapping, actionForm, request, response);
        }
    }
}