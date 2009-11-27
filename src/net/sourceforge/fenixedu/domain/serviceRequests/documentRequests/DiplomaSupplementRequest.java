package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DiplomaSupplementRequest extends DiplomaSupplementRequest_Base {

    public DiplomaSupplementRequest() {
	super();
    }

    public DiplomaSupplementRequest(final DocumentRequestCreateBean bean) {
	this();
	super.init(bean);
	checkParameters(bean);
	super.setRequestedCycle(bean.getRequestedCycle());
	setGivenNames(bean.getGivenNames());
	setFamilyNames(bean.getFamilyNames());
    }

    @Override
    protected void checkParameters(DocumentRequestCreateBean bean) {
	if (bean.getRequestedCycle() == null) {
	    throw new DomainException("error.diplomaSupplementRequest.requestedCycleMustBeGiven");
	} else if (!getDegreeType().getCycleTypes().contains(bean.getRequestedCycle())) {
	    throw new DomainException(
		    "error.diplomaSupplementRequest.requestedDegreeTypeIsNotAllowedForGivenStudentCurricularPlan");
	}
	if (!getRegistration().getStudent().getPerson().getName().equals(bean.getGivenNames() + " " + bean.getFamilyNames())) {
	    throw new DomainException("error.diplomaSupplementRequest.splittedNamesDoNotMatch");
	}
	final DiplomaSupplementRequest supplement = getRegistration().getDiplomaSupplementRequest(bean.getRequestedCycle());
	if (supplement != null && supplement != this) {
	    throw new DomainException("error.diplomaSupplementRequest.alreadyRequested");
	}
    }

    @Override
    public void setRequestedCycle(CycleType requestedCycle) {
	throw new DomainException("error.diplomaSupplementRequest.cannotModifyRequestedCycle");
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.DIPLOMA_SUPPLEMENT_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
	String result = getClass().getName() + "." + getDegreeType().getName();
	if (getRequestedCycle() != null) {
	    result += "." + getRequestedCycle().name();
	}
	return result;
    }

    @Override
    public boolean isPagedDocument() {
	return true;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public EventType getEventType() {
	return null;
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

    @Override
    public boolean isPayedUponCreation() {
	return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return false;
    }

    @Override
    public boolean isToPrint() {
	return !isDelivered();
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);
	if (academicServiceRequestBean.isToProcess()) {
	    if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
		throw new DomainException("error.registryDiploma.registration.not.submited.to.conclusion.process");
	    }
	    RegistryDiplomaRequest registryRequest = getRegistration().getRegistryDiplomaRequest(getRequestedCycle());
	    registryRequest.getRegistryCode().addDocumentRequest(this);
	    getAdministrativeOffice().getCurrentRectorateSubmissionBatch().addDocumentRequest(this);
	}
    }
}
