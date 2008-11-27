package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

import org.joda.time.YearMonthDay;

public class ReceivedAcademicServiceRequestFromExternalEntity extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final AcademicServiceRequest academicServiceRequest, final YearMonthDay receivedDate,
	    final String justification) {
	academicServiceRequest.receivedFromExternalEntity(receivedDate, justification);
    }

}