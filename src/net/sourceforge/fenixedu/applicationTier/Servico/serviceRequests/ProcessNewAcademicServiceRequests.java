package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

public class ProcessNewAcademicServiceRequests extends Service {

    public void run(AcademicServiceRequest academicServiceRequest) {
        academicServiceRequest.process();

    }

}
