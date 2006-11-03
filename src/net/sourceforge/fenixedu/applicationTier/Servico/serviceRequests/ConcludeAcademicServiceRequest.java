package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

public class ConcludeAcademicServiceRequest extends Service {

    public void run(final AcademicServiceRequest academicServiceRequest) {
	academicServiceRequest.conclude();
    }

}
