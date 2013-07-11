package net.sourceforge.fenixedu.applicationTier.Servico.alumni;


import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Job;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditProfessionalInformation {

    @Atomic
    public static void run(final AlumniJobBean jobBean) {

        Job job = FenixFramework.getDomainObject(jobBean.getJobId());
        job.edit(jobBean);
    }
}