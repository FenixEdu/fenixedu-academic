package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;

abstract public class RegistrationAcademicServiceRequestEvent extends RegistrationAcademicServiceRequestEvent_Base {
    
    protected RegistrationAcademicServiceRequestEvent() {
        super();
    }
    
    protected void init(final AdministrativeOffice administrativeOffice, final EventType eventType, final Person person, final RegistrationAcademicServiceRequest academicServiceRequest) {
	super.init(administrativeOffice, eventType, person, academicServiceRequest);
    }
    
    @Override
    public RegistrationAcademicServiceRequest getAcademicServiceRequest() {
        return (RegistrationAcademicServiceRequest) super.getAcademicServiceRequest();
    }
    
    protected Degree getDegree() {
	return getAcademicServiceRequest().getDegree();
    }
}
