package net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.certificates;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.PhdFinalizationCertificateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;

public class PhdFinalizationCertificateRequest extends PhdFinalizationCertificateRequest_Base {
    
    protected PhdFinalizationCertificateRequest() {
        super();
    }

    protected PhdFinalizationCertificateRequest(PhdDocumentRequestCreateBean bean) {
	this();

	this.init(bean);
    }

    @Override
    protected void init(PhdDocumentRequestCreateBean bean) {
	super.init(bean);

	if (!isFree()) {
	    PhdFinalizationCertificateRequestEvent.create(getAdministrativeOffice(), getPerson(), this);
	}

	if (getPhdIndividualProgramProcess().getRegistryDiplomaRequest() == null) {
	    throw new PhdDomainOperationException("error.PhdFinalizationCertificateRequest.registry.diploma.not.requested");
	}

	PhdRegistryDiplomaRequest registryDiplomaRequest = getPhdIndividualProgramProcess().getRegistryDiplomaRequest();

	RectorateSubmissionBatch rectorateSubmissionBatch = registryDiplomaRequest.getRectorateSubmissionBatch();

	if (rectorateSubmissionBatch == null) {
	    throw new PhdDomainOperationException(
		    "error.PhdFinalizationCertificateRequest.registry.diploma.submission.batch.not.sent");
	}

	if (!rectorateSubmissionBatch.isSent()) {
	    throw new PhdDomainOperationException(
		    "error.PhdFinalizationCertificateRequest.registry.diploma.submission.batch.not.sent");
	}
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	try {
	    verifyIsToProcessAndHasPersonalInfo(academicServiceRequestBean);
	    verifyIsToDeliveredAndIsPayed(academicServiceRequestBean);
	} catch (DomainException e) {
	    throw new PhdDomainOperationException(e.getKey(), e, e.getArgs());
	}

	super.internalChangeState(academicServiceRequestBean);
	if (academicServiceRequestBean.isToProcess()) {
	    if (!getPhdIndividualProgramProcess().isConcluded()) {
		throw new PhdDomainOperationException(
			"error.PhdFinalizationCertificateRequest.phd.process.not.submited.to.conclusion.process");
	    }

	    if (getLastGeneratedDocument() == null) {
		generateDocument();
	    }
	}
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.PHD_FINALIZATION_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
	return PhdFinalizationCertificateRequest.class.getName();
    }

    @Override
    public boolean isPayedUponCreation() {
	return true;
    }

    @Override
    public boolean isToPrint() {
	return true;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return false;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
	return false;
    }

    @Override
    public EventType getEventType() {
	return EventType.PHD_FINALIZATION_CERTIFICATE_REQUEST;
    }

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }
    
    public static PhdFinalizationCertificateRequest create(final PhdDocumentRequestCreateBean bean) {
	return new PhdFinalizationCertificateRequest(bean);
    }

}
