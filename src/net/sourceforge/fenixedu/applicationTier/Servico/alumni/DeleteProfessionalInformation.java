package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Job;

public class DeleteProfessionalInformation extends FenixService {

    public void run(Job job) {
	job.delete();
    }

}
