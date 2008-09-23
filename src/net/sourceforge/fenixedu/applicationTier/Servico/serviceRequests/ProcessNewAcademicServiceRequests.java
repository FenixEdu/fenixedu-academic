package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

public class ProcessNewAcademicServiceRequests extends FenixService {

    public void run(AcademicServiceRequest academicServiceRequest) {
	academicServiceRequest.process();

    }

}
