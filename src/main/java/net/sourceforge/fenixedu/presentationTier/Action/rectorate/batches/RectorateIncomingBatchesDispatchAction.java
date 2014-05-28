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
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = RectorateApplication.class, path = "incoming-batches", titleKey = "title.rectorateSubmission.received")
@Mapping(path = "/rectorateIncomingBatches", module = "rectorate")
@Forwards({ @Forward(name = "index", path = "/rectorate/incomingBatches.jsp"),
        @Forward(name = "viewBatch", path = "/rectorate/showBatch.jsp") })
public class RectorateIncomingBatchesDispatchAction extends FenixDispatchAction {

    @EntryPoint
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
        return (IDocumentRequest) FenixFramework.getDomainObject(getRequestParameterAsString(request, "documentRequestId"));
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