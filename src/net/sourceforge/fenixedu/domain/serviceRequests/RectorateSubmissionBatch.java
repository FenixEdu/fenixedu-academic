package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class RectorateSubmissionBatch extends RectorateSubmissionBatch_Base {
    public RectorateSubmissionBatch(AdministrativeOffice administrativeOffice) {
	super();
	setCreation(new DateTime());
	setState(RectorateSubmissionState.UNSENT);
	setAdministrativeOffice(administrativeOffice);
	setRootDomainObject(RootDomainObject.getInstance());
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

    @Service
    public void closeBatch() {
	if (!getState().equals(RectorateSubmissionState.UNSENT))
	    throw new DomainException("error.rectorateSubmission.attemptingToCloseABatchNotInUnsentState");
	setState(RectorateSubmissionState.CLOSED);
	new RectorateSubmissionBatch(getAdministrativeOffice());
    }

    @Service
    public void markAsSent() {
	if (!getState().equals(RectorateSubmissionState.CLOSED))
	    throw new DomainException("error.rectorateSubmission.attemptingToSendABatchNotInClosedState");
	setState(RectorateSubmissionState.SENT);
	setSubmission(new DateTime());
	Employee employee = AccessControl.getPerson().getEmployee();
	for (DocumentRequest document : getDocumentRequestSet()) {
	    document.edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.CONCLUDED, employee,
		    "Insert Template Text Here"));
	    document.edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY, employee,
		    "Insert Template Text Here"));
	}
    }

    @Service
    public void markAsReceived() {
	if (!getState().equals(RectorateSubmissionState.SENT))
	    throw new DomainException("error.rectorateSubmission.attemptingToReceiveABatchNotInSentState");
	setState(RectorateSubmissionState.RECEIVED);
	setReception(new DateTime());
	Employee employee = AccessControl.getPerson().getEmployee();
	for (DocumentRequest document : getDocumentRequestSet()) {
	    document.edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY,
		    employee));
	}
    }
}
