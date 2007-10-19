package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DiplomaRequest extends DiplomaRequest_Base {
    
    private DiplomaRequest() {
        super();
    }

    public DiplomaRequest(final Registration registration, final CycleType requestedCyle) {
	this();
	super.init(registration, Boolean.FALSE, Boolean.FALSE);
	if (getDegreeType().isComposite()) {
	    this.checkParameters(requestedCyle);
	    super.setRequestedCycle(requestedCyle);
	}
    }

    final private void checkParameters(final CycleType requestedCyle) {
	if (requestedCyle == null) {
	    throw new DomainException("DiplomaRequest.diploma.requested.cycle.must.be.given");
	} else if (!getDegreeType().getCycleTypes().contains(requestedCyle)) {
	    throw new DomainException("DiplomaRequest.diploma.requested.degree.type.is.not.allowed.for.given.student.curricular.plan");
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
	
	return getDescription("AcademicServiceRequestType.DOCUMENT", getDocumentRequestType().getQualifiedName() + "." + degreeType.name() + (degreeType.isComposite() ? "." + requestedCycle.name() : ""));
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
	case BOLONHA_PHD_PROGRAM:
	    return EventType.BOLONHA_PHD_PROGRAM_DIPLOMA_REQUEST;
	default:
	    throw new DomainException("DiplomaRequest.not.available.for.given.degree.type");
	}
    }

    @Override
    final public ExecutionYear getExecutionYear() {
	return null;
    }

    @Override
    final protected void internalChangeState(final AcademicServiceRequestSituationType academicServiceRequestSituationType, final Employee employee) {
	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.PROCESSING) {
	    if (NOT_AVAILABLE.contains(getRegistration().getDegreeType())) {
		throw new DomainException("DiplomaRequest.diploma.not.available");
	    }
	    
	    if (!getRegistration().hasConcludedCycle(getRequestedCycle())) {
		throw new DomainException("DiplomaRequest.registration.hasnt.concluded.requested.cycle");
	    }
	    
	    if (hasDissertationTitle() && !getRegistration().hasDissertationThesis()) {
		throw new DomainException("DiplomaRequest.registration.doesnt.have.dissertation.thesis");
	    }
	    
	} else if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED && !isFree()) {
	    DiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
	}
    }

    static final private List<DegreeType> NOT_AVAILABLE = Arrays.asList(new DegreeType[] {
	    DegreeType.MASTER_DEGREE, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA,
	    DegreeType.BOLONHA_SPECIALIZATION_DEGREE });
    
    final public boolean hasFinalAverageDescription() {
	return !hasDissertationTitle();
    }

    final public boolean hasDissertationTitle() {
	return getDegreeType() == DegreeType.MASTER_DEGREE;
    }

}
