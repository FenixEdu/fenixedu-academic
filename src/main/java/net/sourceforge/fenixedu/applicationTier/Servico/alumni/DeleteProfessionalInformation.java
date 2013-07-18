package net.sourceforge.fenixedu.applicationTier.Servico.alumni;


import net.sourceforge.fenixedu.domain.Job;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteProfessionalInformation {

    @Service
    public static void run(Job job) {
        job.delete();
    }

}