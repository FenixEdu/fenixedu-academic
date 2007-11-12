package net.sourceforge.fenixedu.domain.serviceRequests;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class FreeSolicitationAcademicRequest extends FreeSolicitationAcademicRequest_Base {
    
    protected FreeSolicitationAcademicRequest() {
        super();
    }
    
    public FreeSolicitationAcademicRequest(final Registration registration, final ExecutionYear executionYear, final String purpose, final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	super.init(registration, executionYear, urgentRequest, freeProcessed);
	checkParameters(purpose);
	super.setPurpose(purpose);
    }
    
    private void checkParameters(final String purpose) {
	if (StringUtils.isEmpty(purpose)) {
	    throw new DomainException("error.FreeSolicitationAcademicRequest.invalid.purpose");
	}
    }

    @Override
    public void setPurpose(String purpose) {
        throw new DomainException("error.FreeSolicitationAcademicRequest.cannot.modify.purpose");
    }

    @Override
    public String getDescription() {
        return getDescription("AcademicServiceRequestType.FREE_SOLICITATION_ACADEMIC_REQUEST");
    }
    
    @Override
    public EventType getEventType() {
	return EventType.FREE_SOLICITATION_ACADEMIC_REQUEST;
    }
}
