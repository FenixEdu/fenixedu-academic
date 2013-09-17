package net.sourceforge.fenixedu.applicationTier.Servico.alumni;


import net.sourceforge.fenixedu.domain.Job;
import pt.ist.fenixframework.Atomic;

public class DeleteProfessionalInformation {

    @Atomic
    public static void run(Job job) {
        job.delete();
    }

}