package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.RegistryDiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class RegistryDiplomaRequest extends RegistryDiplomaRequest_Base {

    public RegistryDiplomaRequest() {
	super();
    }

    public RegistryDiplomaRequest(final DocumentRequestCreateBean bean) {
	this();
	super.init(bean);
	checkParameters(bean);
	super.setRequestedCycle(bean.getRequestedCycle());
	if (isPayedUponCreation() && !isFree()) {
	    RegistryDiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
	}
	setDiplomaSupplement(new DiplomaSupplementRequest(bean));
    }

    @Override
    protected void checkParameters(DocumentRequestCreateBean bean) {
	if (bean.getRequestedCycle() == null) {
	    throw new DomainException("error.registryDiplomaRequest.requestedCycleMustBeGiven");
	} else if (!getDegreeType().getCycleTypes().contains(bean.getRequestedCycle())) {
	    throw new DomainException("error.registryDiplomaRequest.requestedDegreeTypeIsNotAllowedForGivenStudentCurricularPlan");
	}
	final RegistryDiplomaRequest diploma = getRegistration().getRegistryDiplomaRequest(bean.getRequestedCycle());
	if (diploma != null && diploma != this) {
	    throw new DomainException("error.registryDiplomaRequest.alreadyRequested");
	}
    }

    @Override
    public void setRequestedCycle(CycleType requestedCycle) {
	throw new DomainException("error.registryDiplomaRequest.cannotModifyRequestedCycle");
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.REGISTRY_DIPLOMA_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public boolean isPagedDocument() {
	return false;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public EventType getEventType() {
	switch (getDegreeType()) {
	case BOLONHA_DEGREE:
	    return EventType.BOLONHA_DEGREE_REGISTRY_DIPLOMA_REQUEST;
	case BOLONHA_MASTER_DEGREE:
	    return EventType.BOLONHA_MASTER_DEGREE_REGISTRY_DIPLOMA_REQUEST;
	case BOLONHA_INTEGRATED_MASTER_DEGREE:
	    return (getRequestedCycle() == CycleType.FIRST_CYCLE) ? EventType.BOLONHA_DEGREE_REGISTRY_DIPLOMA_REQUEST
		    : EventType.BOLONHA_MASTER_DEGREE_REGISTRY_DIPLOMA_REQUEST;
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    return EventType.BOLONHA_ADVANCED_FORMATION_REGISTRY_DIPLOMA_REQUEST;
	default:
	    throw new DomainException("error.registryDiploma.not.available.for.given.degree.type");
	}
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

    @Override
    public boolean isPayedUponCreation() {
	return true;
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
	    if (!getFreeProcessed()) {
		assertPayedEvents();
	    }
	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }
	    getRootDomainObject().getInstitutionUnit().getRegistryCodeGenerator().createRegistryFor(this);
	}
    }
}
