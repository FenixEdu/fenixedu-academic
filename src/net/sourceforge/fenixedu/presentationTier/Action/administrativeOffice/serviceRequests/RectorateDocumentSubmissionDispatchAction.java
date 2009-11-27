package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionState;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Archive;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.DiskZipArchive;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Fetcher;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Resource;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.excel.SimplifiedSpreadsheetBuilder;
import pt.utl.ist.fenix.tools.excel.WorkbookExportFormat;

@Mapping(path = "/rectorateDocumentSubmission", module = "academicAdminOffice")
@Forwards( { @Forward(name = "index", path = "/academicAdminOffice/rectorateDocumentSubmission/batchIndex.jsp"),
	@Forward(name = "viewBatch", path = "/academicAdminOffice/rectorateDocumentSubmission/showBatch.jsp") })
public class RectorateDocumentSubmissionDispatchAction extends FenixDispatchAction {
    public ActionForward index(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	AdministrativeOffice office = getLoggedPerson(request).getEmployee().getAdministrativeOffice();
	request.setAttribute("unsent", office.getRectorateSubmissionBatchesByState(RectorateSubmissionState.UNSENT));
	request.setAttribute("closed", office.getRectorateSubmissionBatchesByState(RectorateSubmissionState.CLOSED));
	request.setAttribute("sent", office.getRectorateSubmissionBatchesByState(RectorateSubmissionState.SENT));
	request.setAttribute("received", office.getRectorateSubmissionBatchesByState(RectorateSubmissionState.RECEIVED));
	return mapping.findForward("index");
    }

    public ActionForward viewBatch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	Set<String> actions = new HashSet<String>();
	switch (batch.getState()) {
	case UNSENT:
	    if (batch.hasAnyDocumentRequest()) {
		actions.add("closeBatch");
	    }
	    break;
	case CLOSED:
	    actions.add("generateMetadata");
	    actions.add("zipDocuments");
	    actions.add("markAsSent");
	    break;
	case SENT:
	    actions.add("generateMetadata");
	    actions.add("zipDocuments");
	    actions.add("markAsReceived");
	case RECEIVED:
	    actions.add("generateMetadata");
	    actions.add("zipDocuments");
	}
	request.setAttribute("batch", batch);
	request.setAttribute("requests", batch.getDocumentRequestSet());
	request.setAttribute("actions", actions);
	return mapping.findForward("viewBatch");
    }

    public ActionForward closeBatch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	batch.closeBatch();
	return viewBatch(mapping, actionForm, request, response);
    }

    public ActionForward markAsSent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	batch.markAsSent();
	return viewBatch(mapping, actionForm, request, response);
    }

    public ActionForward markAsReceived(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	batch.markAsReceived();
	return viewBatch(mapping, actionForm, request, response);
    }

    public ActionForward generateMetadata(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	SimplifiedSpreadsheetBuilder<DocumentRequest> builder = new SimplifiedSpreadsheetBuilder<DocumentRequest>(batch
		.getDocumentRequestSet()) {
	    private final ResourceBundle enumeration = ResourceBundle.getBundle("resources/EnumerationResources", new Locale(
		    "pt", "PT"));

	    @Override
	    protected void makeLine(DocumentRequest document) {
		addColumn("Código", document.getRegistryCode().getCode());
		addColumn("Tipo de Documento", enumeration.getString(document.getDocumentRequestType().name()));
		switch (document.getDocumentRequestType()) {
		case REGISTRY_DIPLOMA_REQUEST:
		    addColumn("Ciclo", enumeration.getString(((RegistryDiplomaRequest) document).getRequestedCycle().name()));
		    break;
		case DIPLOMA_REQUEST:
		    addColumn("Ciclo", enumeration.getString(((DiplomaRequest) document).getWhatShouldBeRequestedCycle().name()));
		    break;
		case DIPLOMA_SUPPLEMENT_REQUEST:
		    addColumn("Ciclo", enumeration.getString(((DiplomaSupplementRequest) document).getRequestedCycle().name()));
		    break;
		default:
		    addColumn("Ciclo", null);
		}
		addColumn("Tipo de Curso", enumeration.getString(document.getDegreeType().name()));
		addColumn("Nº de Aluno", document.getStudent().getNumber());
		addColumn("Nome", document.getPerson().getName());
		addColumn("Ficheiro", document.getLastGeneratedDocument().getFilename());
	    }
	};
	try {
	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-disposition", "attachment; filename=" + batch.getRange() + ".xls");
	    final ServletOutputStream writer = response.getOutputStream();
	    builder.build(WorkbookExportFormat.EXCEL, writer);
	    writer.flush();
	    response.flushBuffer();
	} catch (IOException e) {
	    throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata");
	}

	return null;
    }

    public ActionForward zipDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	    Archive archive = new DiskZipArchive(response, batch.getRange());
	    Fetcher fetcher = new Fetcher(archive, request, response);
	    for (DocumentRequest document : batch.getDocumentRequestSet()) {
		fetcher.queue(new Resource(document.getLastGeneratedDocument().getFilename(), document.getLastGeneratedDocument()
			.getDownloadUrl()));
	    }

	    fetcher.process();
	    archive.finish();
	} catch (IOException e) {
	    throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata");
	} catch (ServletException e) {
	    throw new DomainException("error.rectorateSubmission.errorGeneratingMetadata");
	}

	return null;
    }
}
