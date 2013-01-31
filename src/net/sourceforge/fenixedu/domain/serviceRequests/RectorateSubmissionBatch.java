package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class RectorateSubmissionBatch extends RectorateSubmissionBatch_Base {
	public RectorateSubmissionBatch(AdministrativeOffice administrativeOffice) {
		super();
		setCreation(new DateTime());
		setCreator(AccessControl.hasPerson() ? AccessControl.getPerson() : null);
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
		for (AcademicServiceRequest request : getDocumentRequestSet()) {
			RegistryCode code = null;
			if (request.isRegistryDiploma()) {
				code = request.getRegistryCode();
			} else if (request.isDiploma() && request.isRequestForRegistration()) {
				// FIXME: this can leave after all diplomas without registry
				// diplomas are dealt with.
				DiplomaRequest diploma = (DiplomaRequest) request;
				if (!diploma.hasRegistryDiplomaRequest()) {
					code = diploma.getRegistryCode();
				}
			}

			if (code == null) {
				continue;
			}

			if (first == null || code.getCode().compareTo(first) < 0) {
				first = code.getCode();
			}
			if (last == null || code.getCode().compareTo(last) > 0) {
				last = code.getCode();
			}
		}

		if (first != null && last != null) {
			return first + "-" + last;
		}
		return "-";
	}

	public int getDiplomaDocumentRequestCount() {
		int acc = 0;

		for (AcademicServiceRequest docRequest : getDocumentRequestSet()) {
			if ((docRequest.isRegistryDiploma()) || (docRequest.isDiplomaSupplement())) {
				acc++;
			}
		}

		return acc;
	}

	public RectorateSubmissionBatch closeBatch() {
		if (!getState().equals(RectorateSubmissionState.UNSENT)) {
			throw new DomainException("error.rectorateSubmission.attemptingToCloseABatchNotInUnsentState");
		}
		setState(RectorateSubmissionState.CLOSED);
		return new RectorateSubmissionBatch(getAdministrativeOffice());
	}

	public void markAsSent() {
		if (!getState().equals(RectorateSubmissionState.CLOSED)) {
			throw new DomainException("error.rectorateSubmission.attemptingToSendABatchNotInClosedState");
		}
		setState(RectorateSubmissionState.SENT);
		setSubmission(new DateTime());
		setSubmitter(AccessControl.getPerson());
		for (AcademicServiceRequest document : getDocumentRequestSet()) {
			if (document.isCancelled() || document.isRejected()) {
				continue;
			}
			document.edit(new AcademicServiceRequestBean(AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY,
					AccessControl.getPerson(), "Insert Template Text Here"));
		}
	}

	public boolean allDocumentsReceived() {
		for (AcademicServiceRequest document : getDocumentRequestSet()) {
			AcademicServiceRequestSituationType type = document.getAcademicServiceRequestSituationType();
			if (!type.equals(AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY)
					&& !type.equals(AcademicServiceRequestSituationType.DELIVERED)) {
				return false;
			}
		}
		return true;
	}

	public void markAsReceived() {
		if (!getState().equals(RectorateSubmissionState.SENT)) {
			throw new DomainException("error.rectorateSubmission.attemptingToReceiveABatchNotInSentState");
		}
		if (allDocumentsReceived()) {
			setState(RectorateSubmissionState.RECEIVED);
			setReception(new DateTime());
			setReceptor(AccessControl.getPerson());
		}
	}

	public void delete() {
		if (hasAnyDocumentRequest()) {
			throw new DomainException("error.rectorateSubmission.cannotDeleteBatchWithDocuments");
		}
		removeCreator();
		removeReceptor();
		removeSubmitter();
		removeAdministrativeOffice();
		removeRootDomainObject();
		super.deleteDomainObject();
	}

	@Override
	public int getDocumentRequestCount() {
		int result = 0;

		Set<AcademicServiceRequest> documentRequestSet = getDocumentRequestSet();

		for (AcademicServiceRequest academicServiceRequest : documentRequestSet) {
			if (academicServiceRequest.isRejected()) {
				continue;
			}

			if (academicServiceRequest.isCancelled()) {
				continue;
			}

			result++;
		}

		return result;
	}

	public static Set<RectorateSubmissionBatch> getRectorateSubmissionBatchesByState(Collection<AdministrativeOffice> offices,
			RectorateSubmissionState state) {
		Set<RectorateSubmissionBatch> result = new HashSet<RectorateSubmissionBatch>();
		for (AdministrativeOffice office : offices) {
			for (RectorateSubmissionBatch batch : office.getRectorateSubmissionBatchSet()) {
				if (batch.getState().equals(state)) {
					result.add(batch);
				}
			}
		}
		return result;
	}
}
