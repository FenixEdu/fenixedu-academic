package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class SendAcademicServiceRequestToExternalEntity {

    @Atomic
    public static void run(final AcademicServiceRequest academicServiceRequest, final YearMonthDay sendDate,
            final String justification) {
        academicServiceRequest.sendToExternalEntity(sendDate, justification);
    }

}
