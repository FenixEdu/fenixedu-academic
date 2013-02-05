package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Job;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteProfessionalInformation extends FenixService {

    @Service
    public static void run(Job job) {
        job.delete();
    }

}