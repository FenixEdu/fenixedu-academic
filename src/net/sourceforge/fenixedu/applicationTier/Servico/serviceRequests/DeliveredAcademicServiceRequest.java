package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

public class DeliveredAcademicServiceRequest extends FenixService {

    public void run(final AcademicServiceRequest academicServiceRequest) {
	academicServiceRequest.delivered();
    }

}
