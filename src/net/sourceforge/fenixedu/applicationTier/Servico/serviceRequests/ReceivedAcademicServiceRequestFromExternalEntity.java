package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

import org.joda.time.YearMonthDay;

public class ReceivedAcademicServiceRequestFromExternalEntity extends Service {

    public void run(final AcademicServiceRequest academicServiceRequest, final YearMonthDay receivedDate,
	    final String justification) {
	academicServiceRequest.receivedFromExternalEntity(receivedDate, justification);
    }

}
