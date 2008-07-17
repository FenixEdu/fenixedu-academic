package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Job;

public class EditProfessionalInformation extends Service {

    public void run(final AlumniJobBean jobBean) {

	Job job = rootDomainObject.readJobByOID(jobBean.getJobId());
	job.setEmployerName(jobBean.getEmployerName());
	job.setCity(jobBean.getCity());
	job.setCountry(jobBean.getCountry());
	job.setBusinessArea(jobBean.getChildBusinessArea());
	job.setPosition(jobBean.getPosition());
	job.setBeginDate(jobBean.getBeginDateAsLocalDate());
	job.setEndDate(jobBean.getEndDateAsLocalDate());
	job.setContractType(jobBean.getContractType());
    }
}
