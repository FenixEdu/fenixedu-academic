package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

abstract public class PhdAcademicServiceRequestEvent extends PhdAcademicServiceRequestEvent_Base {

    protected PhdAcademicServiceRequestEvent() {
        super();
    }

    @Override
    protected void init(final AdministrativeOffice administrativeOffice, final EventType eventType, final Person person,
            final AcademicServiceRequest academicServiceRequest) {
        if (!academicServiceRequest.isRequestForPhd()) {
            throw new DomainException("PhdAcademicServiceRequestEvent.request.is.not.for.phd");
        }

        super.init(administrativeOffice, eventType, person, academicServiceRequest);
    }

}
