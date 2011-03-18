package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class RectorateSubmissionBatch extends RectorateSubmissionBatch_Base {
    public RectorateSubmissionBatch(AdministrativeOffice administrativeOffice) {
	super();
	setCreation(new DateTime());
	setCreator(AccessControl.hasPerson() ? AccessControl.getPerson().getEmployee() : null);
	setState(RectorateSubmissionState.UNSENT);
	setAdministrativeOffice(administrativeOffice);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean isUnsent() {
	return getState().equals(RectorateSubmissionState.UNSENT);
    }

    public boolean isClosed() {
	return getState().equals(RectorateSubmissionState.CLOSED);
    }

    public boolean isSent() {
	return getState().equals(RectorateSubmissionState.SENT);
    }

    public boolean isReceived() {
	return getState().equals(RectorateSubmissionState.RECEIVED);
    }

    public RectorateSubmissionBatch getNextRectorateSubmissionBatch() {
	RectorateSubmissionBatch next = null;
	for (RectorateSubmissionBatch batch : getAdministrativeOffice().getRectorateSubmissionBatchSet()) {
	    if (batch.getCreation().isAfter(getCreation()) && (next == null || batch.getCreation().isBefore(next.getCreation()))) {
		next = batch;
	    }
	}
	return next;
    }

    public RectorateSubmissionBatch getPreviousRectorateSubmissionBatch() {
	RectorateSubmissionBatch previous = null;
	for (RectorateSubmissionBatch batch : getAdministrativeOffice().getRectorateSubmissionBatchSet()) {
	    if (batch.getCreation().isBefore(getCreation())
		    && (previous == null || batch.getCreation().isAfter(previous.getCreation()))) {
		previous = batch;
	    }
	}
	return previous;
    }

    public String getRange() {
	String first = null;
	String last = null;
	for (DocumentRequest request : getDocumentRequestSet()) {
	    if (request instanceof RegistryDiplomaRequest) {
		RegistryCode code = request.getRegistryCode();
		if (first == null || code.getCode().compareTo(first) < 0) {
		    first = code.getCode();
		}
		if (last == null || code.getCode().compareTo(last) > 0) {
		    last = code.getCode();
		}
	    } else if (request instanceof DiplomaRequest) {
		// FIXME: this can leave after all diplomas without registry
		// diplomas are dealt with.
		DiplomaRequest diploma = (DiplomaRequest) request;
		if (diploma.getRegistration().getRegistryDiplomaRequest(diploma.getWhatShouldBeRequestedCycle()) == null) {
		    RegistryCode code = diploma.getRegistryCode();
		    if (first == null || code.getCode().compareTo(first) < 0) {
			first = code.getCode();
		    }
		    if (last == null || code.getCode().compareTo(last) > 0) {
			last = code.getCode();
		    }
		}
	    }
	}
	if (first != null && last != null) {
	    return first + "-" + last;
	} else {
	    return "-";
	}
    }

    public int getDiplomaDocumentRequestCount() {
	int acc = 0;

	for (DocumentRequest docRequest : getDocumentRequestSet()) {
	    if ((docRequest instanceof RegistryDiplomaRequest) || (docRequest instanceof DiplomaSupplementRequest)) {
		acc++;
	    }
	}

	return acc;
    }

    public void closeBatch() {
	if (!getState().equals(RectorateSubmissionState.UNSENT))
	    throw new DomainException("error.rectorateSubmission.attemptingToCloseABatchNotInUnsentState");
	setState(RectorateSubmissionState.CLOSED);
	new RectorateSubmissionBatch(getAdministrativeOffice());
    }

    public void markAsSent() {
	if (!getState().equals(RectorateSubmissionState.CLOSED))
	    throw new DomainException("error.rectorateSubmission.attemptingToSendABatchNotInClosedState");
	setState(RectorateSubmissionState.SENT);
	setSubmission(new DateTime());
	setSubmitter(AccessControl.getPerson().getEmployee());
	Employee employee = AccessControl.getPerson().getEmployee();
	for (DocumentRequest document : getDocumentRequestSet()) {
	    if (document.isCancelled() || document.isRejected()) {
		continue;
	    }
	    document.edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY, employee,
		    "Insert Template Text Here"));
	}
    }

    public boolean allDocumentsReceived() {
	for (DocumentRequest document : getDocumentRequestSet()) {
	    if (!document.getAcademicServiceRequestSituationType().equals(
		    AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY)) {
		return false;
	    }
	}
	return true;
    }

    public void markAsReceived() {
	if (!getState().equals(RectorateSubmissionState.SENT))
	    throw new DomainException("error.rectorateSubmission.attemptingToReceiveABatchNotInSentState");
	if (allDocumentsReceived()) {
	    setState(RectorateSubmissionState.RECEIVED);
	    setReception(new DateTime());
	    setReceptor(AccessControl.getPerson().getEmployee());
	}
    }

    public void delete() {
	if (hasAnyDocumentRequest()) {
	    throw new DomainException("error.rectorateSubmission.cannotDeleteBatchWithDocuments");
	}
	removeAdministrativeOffice();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
