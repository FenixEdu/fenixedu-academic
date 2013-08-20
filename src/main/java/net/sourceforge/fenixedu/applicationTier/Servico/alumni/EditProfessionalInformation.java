package net.sourceforge.fenixedu.applicationTier.Servico.alumni;


import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Job;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class EditProfessionalInformation {

    @Service
    public static void run(final AlumniJobBean jobBean) {

        Job job = AbstractDomainObject.fromExternalId(jobBean.getJobId());
        job.edit(jobBean);
    }
}