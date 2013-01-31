package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Job;
import pt.ist.fenixWebFramework.services.Service;

public class EditProfessionalInformation extends FenixService {

	@Service
	public static void run(final AlumniJobBean jobBean) {

		Job job = rootDomainObject.readJobByOID(jobBean.getJobId());
		job.edit(jobBean);
	}
}