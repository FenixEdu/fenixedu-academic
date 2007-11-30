package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DiplomaRequest extends DiplomaRequest_Base {
    
    private DiplomaRequest() {
        super();
    }

    public DiplomaRequest(final Registration registration, final CycleType requestedCycle) {
	this();
	
	this.init(registration, requestedCycle);
    }

    final private void init(final Registration registration, final CycleType requestedCycle) {
	super.init(registration, Boolean.FALSE, Boolean.FALSE);
	
	this.checkParameters(requestedCycle);
	
	if (!isFree()) {
	    DiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
	}
    }

    final private void checkParameters(final CycleType requestedCycle) {
	if (getDegreeType().isComposite()) {
	    if (requestedCycle == null) {
		throw new DomainException("DiplomaRequest.diploma.requested.cycle.must.be.given");
	    } else if (!getDegreeType().getCycleTypes().contains(requestedCycle)) {
		throw new DomainException("DiplomaRequest.diploma.requested.degree.type.is.not.allowed.for.given.student.curricular.plan");
	    }

	    super.setRequestedCycle(requestedCycle);
	}
	
	checkForDuplicate(requestedCycle);
    }
    
    private void checkForDuplicate(final CycleType requestedCycle) {
	if (getRegistration().getDiplomaRequest(requestedCycle) != this) {
	    throw new DomainException("DiplomaRequest.diploma.already.requested");
	}
    }

    @Override
    final public void setRequestedCycle(final CycleType requestedCycle) {
	throw new DomainException("DiplomaRequest.cannot.modify.requestedCycle");
    }

    @Override
    final public String getDescription() {
	final DegreeType degreeType = getDegreeType();
	final CycleType requestedCycle = getRequestedCycle();
	
	return getDescription(AcademicServiceRequestType.DOCUMENT, getDocumentRequestType().getQualifiedName() + "." + degreeType.name() + (degreeType.isComposite() ? "." + requestedCycle.name() : ""));
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.DIPLOMA_REQUEST;
    }

    @Override
    final public String getDocumentTemplateKey() {
	String result = getClass().getName() + "." +  getDegreeType().getName();  
	if (getRequestedCycle() != null) {
	    result += "." + getRequestedCycle().name();
	}
	
	return result;
    }

    @Override
    final public EventType getEventType() {
	switch (getDegreeType()) {
	case DEGREE:
	case BOLONHA_DEGREE:
	    return EventType.BOLONHA_DEGREE_DIPLOMA_REQUEST;
	case MASTER_DEGREE:
	case BOLONHA_MASTER_DEGREE:
	    return EventType.BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST;
	case BOLONHA_INTEGRATED_MASTER_DEGREE:
	    return (getRequestedCycle() == CycleType.FIRST_CYCLE) ? EventType.BOLONHA_DEGREE_DIPLOMA_REQUEST : EventType.BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST;
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    return EventType.BOLONHA_ADVANCED_FORMATION_DIPLOMA_REQUEST;
	case BOLONHA_PHD_PROGRAM:
	    return EventType.BOLONHA_PHD_PROGRAM_DIPLOMA_REQUEST;
	default:
	    throw new DomainException("DiplomaRequest.not.available.for.given.degree.type");
	}
    }

    @Override
    final protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	if (academicServiceRequestBean.isToProcess()) {
	    if (getRegistration().hasGratuityDebtsCurrently() && !getFreeProcessed()) {
		throw new DomainException("DocumentRequest.registration.has.not.payed.gratuities");
	    }
	    
	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }

	    checkForDuplicate(getRequestedCycle());
	    
	    if (NOT_AVAILABLE.contains(getRegistration().getDegreeType())) {
		throw new DomainException("DiplomaRequest.diploma.not.available");
	    }
	    
	    if (!getRegistration().isRegistrationConclusionProcessed(getRequestedCycle())) {
		throw new DomainException("DiplomaRequest.registration.not.submited.to.conclusion.process");
	    }
	    
	    if (hasDissertationTitle() && !getRegistration().hasDissertationThesis()) {
		throw new DomainException("DiplomaRequest.registration.doesnt.have.dissertation.thesis");
	    }
	}
	
	if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
	    getEvent().cancel(academicServiceRequestBean.getEmployee());
	}
    }

    static final private List<DegreeType> NOT_AVAILABLE = Arrays.asList(new DegreeType[] {
	    DegreeType.BOLONHA_PHD_PROGRAM,
	    DegreeType.BOLONHA_SPECIALIZATION_DEGREE});
    
    final public boolean hasFinalAverageDescription() {
	return !hasDissertationTitle();
    }

    final public boolean hasDissertationTitle() {
	return getDegreeType() == DegreeType.MASTER_DEGREE;
    }

}
