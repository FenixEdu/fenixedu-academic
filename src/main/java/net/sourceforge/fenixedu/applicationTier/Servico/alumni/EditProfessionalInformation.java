package net.sourceforge.fenixedu.applicationTier.Servico.alumni;


import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class EditProfessionalInformation {

    @Service
    public static void run(final AlumniJobBean jobBean) {

        Job job = RootDomainObject.getInstance().readJobByOID(jobBean.getJobId());
        job.edit(jobBean);
    }
}