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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.serviceRequests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.IProgramConclusionRequest;
import org.fenixedu.academic.domain.serviceRequests.IRegistryDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.RectorateSubmissionBatch;
import org.fenixedu.academic.domain.serviceRequests.RectorateSubmissionState;
import org.fenixedu.academic.domain.serviceRequests.RegistryCode;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminServicesApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.SheetData;
import org.fenixedu.commons.spreadsheet.SpreadsheetBuilder;
import org.fenixedu.commons.spreadsheet.WorkbookExportFormat;

import pt.ist.fenixframework.Atomic;

@StrutsFunctionality(app = AcademicAdminServicesApp.class, path = "rectorate-submission", titleKey = "link.rectorateSubmission",
        accessGroup = "academic(SERVICE_REQUESTS_RECTORAL_SENDING)")
@Mapping(path = "/rectorateDocumentSubmission", module = "academicAdministration")
@Forwards({ @Forward(name = "index", path = "/academicAdminOffice/rectorateDocumentSubmission/batchIndex.jsp"),
        @Forward(name = "viewBatch", path = "/academicAdminOffice/rectorateDocumentSubmission/showBatch.jsp") })
public class RectorateDocumentSubmissionDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward index(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Set<AdministrativeOffice> offices =
                AcademicAccessRule.getOfficesAccessibleToFunction(AcademicOperationType.SERVICE_REQUESTS_RECTORAL_SENDING,
                        getLoggedPerson(request).getUser()).collect(Collectors.toSet());

        request.setAttribute("unsent",
                RectorateSubmissionBatch.getRectorateSubmissionBatchesByState(offices, RectorateSubmissionState.UNSENT));
        request.setAttribute("closed",
                RectorateSubmissionBatch.getRectorateSubmissionBatchesByState(offices, RectorateSubmissionState.CLOSED));
        request.setAttribute("sent",
                RectorateSubmissionBatch.getRectorateSubmissionBatchesByState(offices, RectorateSubmissionState.SENT));
        request.setAttribute("received",
                RectorateSubmissionBatch.getRectorateSubmissionBatchesByState(offices, RectorateSubmissionState.RECEIVED));
        return mapping.findForward("index");
    }

    public ActionForward viewBatch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
        Set<String> actions = new HashSet<String>();
        Set<String> confirmActions = new HashSet<String>();
        switch (batch.getState()) {
        case UNSENT:
            if (!batch.getDocumentRequestSet().isEmpty()) {
                confirmActions.add("closeBatch");
            }
            break;
        case CLOSED:
            actions.add("generateMetadataForRegistry");
            actions.add("generateMetadataForDiplomas");
            actions.add("zipDocuments");
            confirmActions.add("markAsSent");
            break;
        case SENT:
            actions.add("generateMetadataForDiplomas");
            if (batch.allDocumentsReceived()) {
                confirmActions.add("markAsReceived");
            }
            break;
        case RECEIVED:
            break;
        }
        request.setAttribute("batch", batch);
        // Filter out canceled document requests, ticket: #248539
        Set<AcademicServiceRequest> requests = new HashSet<AcademicServiceRequest>();
        for (AcademicServiceRequest docRequest : batch.getDocumentRequestSet()) {
            if (!docRequest.isCancelled() && !docRequest.isRejected()) {
                requests.add(docRequest);
            }
        }
        request.setAttribute("requests", requests);
        request.setAttribute("actions", actions);
        request.setAttribute("confirmActions", confirmActions);
        return mapping.findForward("viewBatch");
    }

    @Atomic
    public ActionForward closeBatch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
        batch.closeBatch();
        return viewBatch(mapping, actionForm, request, response);
    }

    @Atomic
    public ActionForward markAsSent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
        batch.markAsSent();
        return viewBatch(mapping, actionForm, request, response);
    }

    @Atomic
    public ActionForward markAsReceived(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
        batch.markAsReceived();
        return viewBatch(mapping, actionForm, request, response);
    }

    public ActionForward generateMetadataForRegistry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
        Set<AcademicServiceRequest> docs = new HashSet<AcademicServiceRequest>();
        for (AcademicServiceRequest document : batch.getDocumentRequestSet()) {
            // Filter out canceled document requests, ticket: #248539
            if (!document.isDiploma() && !document.isCancelled() && !document.isRejected()) {
                docs.add(document);
            }
        }
        return generateMetadata(docs, "registos-", request, response);
    }

    public ActionForward generateMetadataForDiplomas(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
        Set<AcademicServiceRequest> docs = new HashSet<AcademicServiceRequest>();
        for (AcademicServiceRequest document : batch.getDocumentRequestSet()) {
            // Filter out canceled document requests, ticket: #248539
            if (document.isDiploma() && !document.isCancelled() && !document.isRejected()) {
                docs.add(document);
            }
        }
        return generateMetadata(docs, "cartas-", request, response);
    }

    private ActionForward generateMetadata(Set<AcademicServiceRequest> documents, String prefix, HttpServletRequest request,
            HttpServletResponse response) {
        SortedSet<AcademicServiceRequest> sorted =
                new TreeSet<AcademicServiceRequest>(DocumentRequest.COMPARATOR_BY_REGISTRY_NUMBER);

        sorted.addAll(documents);
        Set<RegistryCode> codes = new HashSet<RegistryCode>();
        for (AcademicServiceRequest document : documents) {
            codes.add(document.getRegistryCode());
        }
        SheetData<AcademicServiceRequest> data = new SheetData<AcademicServiceRequest>(sorted) {
            @Override
            protected void makeLine(AcademicServiceRequest academicServiceRequest) {
                IDocumentRequest document = (IDocumentRequest) academicServiceRequest;

                addCell("Código", document.getRegistryCode().getCode());
                addCell("Tipo de Documento", BundleUtil.getString(Bundle.ENUMERATION, document.getDocumentRequestType().name()));

                addCell("Apuramento", document instanceof IProgramConclusionRequest ? ((IProgramConclusionRequest) document)
                        .getProgramConclusion().getName().getContent() : null);

                if (document.isRequestForRegistration()) {
                    addCell("Tipo de Curso", ((DocumentRequest) document).getDegreeType().getName().getContent());
                } else if (document.isRequestForPhd()) {
                    addCell("Tipo de Curso", BundleUtil.getString(Bundle.PHD, "label.php.program"));
                }

                addCell("Nº de Aluno", document.getStudent().getNumber());
                addCell("Nome", document.getPerson().getName());
                if (!document.isDiploma()) {
                    addCell("Ficheiro", document.getLastGeneratedDocument().getFilename());
                }
            }
        };
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + prefix + "-" + "(" + codes.size() + ")" + ".xls");
            final ServletOutputStream writer = response.getOutputStream();
            new SpreadsheetBuilder().addSheet("lote", data).build(WorkbookExportFormat.EXCEL, writer);
            writer.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata", e);
        }

        return null;
    }

    public ActionForward zipDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
            Set<AcademicServiceRequest> requestsToZip = new HashSet<AcademicServiceRequest>();
            for (AcademicServiceRequest document : batch.getDocumentRequestSet()) {
                // Filter out canceled document requests, ticket: #248539
                if (!document.isDiploma() && !document.isCancelled() && !document.isRejected()) {
                    requestsToZip.add(document);
                }
            }
            if (!requestsToZip.isEmpty()) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                ZipOutputStream zip = new ZipOutputStream(bout);
                for (AcademicServiceRequest document : requestsToZip) {
                    zip.putNextEntry(new ZipEntry(document.getLastGeneratedDocument().getFilename()));
                    zip.write(document.getLastGeneratedDocument().getContent());
                    zip.closeEntry();
                }
                zip.close();
                response.setContentType("application/zip");
                response.addHeader("Content-Disposition", "attachment; filename=documentos-" + batch.getRange() + ".zip");
                ServletOutputStream writer = response.getOutputStream();
                writer.write(bout.toByteArray());
                writer.flush();
                writer.close();
                response.flushBuffer();
                return null;
            } else {
                addActionMessage(request, "error.rectorateSubmission.noDocumentsToZip");
                request.setAttribute("batchOid", batch.getExternalId());
                return viewBatch(mapping, actionForm, request, response);
            }
        } catch (IOException e) {
            throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata", e);
        }
    }

    @Atomic
    public ActionForward delayRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        AcademicServiceRequest document = getDomainObject(request, "academicServiceRequestOid");
        request.setAttribute("batchOid", document.getRectorateSubmissionBatch().getExternalId());
        if (document.isPiggyBackedOnRegistry()) {
            addActionMessage(request, "error.rectorateSubmissionBatch.cannotDelayPiggyBackedDocument");
        } else {
            RectorateSubmissionBatch target = document.getRectorateSubmissionBatch();
            while (!target.isUnsent()) {
                target = target.getNextRectorateSubmissionBatch();
            }
            if (document.isRegistryDiploma()) {
                IRegistryDiplomaRequest registry = (IRegistryDiplomaRequest) document;
                ((AcademicServiceRequest) registry.getDiplomaSupplement()).setRectorateSubmissionBatch(target);
            }
            document.setRectorateSubmissionBatch(target);
        }
        return viewBatch(mapping, actionForm, request, response);
    }
}
