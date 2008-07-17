package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Job;

public class DeleteProfessionalInformation extends Service {

    public void run(Job job) {
	job.delete();
    }

}
