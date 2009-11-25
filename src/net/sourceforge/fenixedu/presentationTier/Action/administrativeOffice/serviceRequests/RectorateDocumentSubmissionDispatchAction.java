package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionState;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistryCode;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/rectorateDocumentSubmission", module = "academicAdminOffice")
@Forwards( { @Forward(name = "index", path = "/academicAdminOffice/rectorateDocumentSubmission/batchIndex.jsp"),
	@Forward(name = "viewBatch", path = "/academicAdminOffice/rectorateDocumentSubmission/showBatch.jsp") })
public class RectorateDocumentSubmissionDispatchAction extends FenixDispatchAction {
    public ActionForward index(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("unsent", rootDomainObject.getInstitutionUnit().getRegistryCodeGenerator()
		.getRectorateSubmissionBatchesByState(RectorateSubmissionState.UNSENT));
	request.setAttribute("closed", rootDomainObject.getInstitutionUnit().getRegistryCodeGenerator()
		.getRectorateSubmissionBatchesByState(RectorateSubmissionState.CLOSED));
	request.setAttribute("sent", rootDomainObject.getInstitutionUnit().getRegistryCodeGenerator()
		.getRectorateSubmissionBatchesByState(RectorateSubmissionState.SENT));
	request.setAttribute("received", rootDomainObject.getInstitutionUnit().getRegistryCodeGenerator()
		.getRectorateSubmissionBatchesByState(RectorateSubmissionState.RECEIVED));
	return mapping.findForward("index");
    }

    public ActionForward viewBatch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	Set<DocumentRequest> requests = new HashSet<DocumentRequest>();
	for (RegistryCode code : batch.getRegistryCodeSet()) {
	    requests.addAll(code.getDocumentRequestSet());
	}
	Set<String> actions = new HashSet<String>();
	switch (batch.getState()) {
	case UNSENT:
	    actions.add("closeBatch");
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
	request.setAttribute("requests", requests);
	request.setAttribute("actions", actions);
	return mapping.findForward("viewBatch");
    }

    public ActionForward closeBatch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	RectorateSubmissionBatch batch = getDomainObject(request, "batchOid");
	batch.closeBag();
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
	return viewBatch(mapping, actionForm, request, response);
    }

    public ActionForward zipDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return viewBatch(mapping, actionForm, request, response);
    }

}
