package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

import org.joda.time.YearMonthDay;

public class SendAcademicServiceRequestToExternalEntity extends Service {

    public void run(final AcademicServiceRequest academicServiceRequest, final YearMonthDay sendDate, final String justification) {
	academicServiceRequest.sendToExternalEntity(sendDate, justification);
    }

}
