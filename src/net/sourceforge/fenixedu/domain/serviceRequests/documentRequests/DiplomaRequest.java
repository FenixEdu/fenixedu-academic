package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DiplomaRequest extends DiplomaRequest_Base {
    
    public DiplomaRequest() {
        super();
    }

    public DiplomaRequest(final Registration registration) {
	this();
	super.init(registration, Boolean.FALSE, Boolean.FALSE);
    }

    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();
	
	result.add(AdministrativeOfficeType.DEGREE);
	result.add(AdministrativeOfficeType.MASTER_DEGREE);
	
	return result;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.DIPLOMA_REQUEST;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName() + "." +  getDegreeType().getName();
    }

    @Override
    final public EventType getEventType() {

// TODO uncomment once diploma accounting is implemented 
//	
//	switch (getDegreeType()) {
//	case BOLONHA_DEGREE:
//	    return EventType.BOLONHA_DEGREE_DIPLOMA_REQUEST;
//	case BOLONHA_MASTER_DEGREE:
//	    return EventType.BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST;
//	case BOLONHA_PHD_PROGRAM:
//	    return EventType.BOLONHA_PHD_PROGRAM_DIPLOMA_REQUEST;
//	default:
//	    throw new DomainException("DiplomaRequest.not.available.for.given.degree.type");
//	}

	return null;
    }

    @Override
    public ExecutionYear getExecutionYear() {
	return null;
    }

    @Override
    protected void internalChangeState(final AcademicServiceRequestSituationType academicServiceRequestSituationType, final Employee employee) {
	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED) {
	    if (!isFree()) {
		// TODO uncomment once diploma accounting is implemented
		//DiplomaRequestEvent.create(getAdministrativeOffice(), getRegistration().getPerson(), this);
	    }
	}
    }

    @Override
    protected boolean isFree() {
	return false;
    }

}
