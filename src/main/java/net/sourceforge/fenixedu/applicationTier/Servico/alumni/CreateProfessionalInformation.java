package net.sourceforge.fenixedu.applicationTier.Servico.alumni;


import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Job;
import pt.ist.fenixWebFramework.services.Service;

public class CreateProfessionalInformation {

    @Service
    public static Job run(final AlumniJobBean bean) {

        return new Job(bean.getAlumni().getStudent().getPerson(), bean.getEmployerName(), bean.getCity(), bean.getCountry(),
                bean.getChildBusinessArea(), bean.getParentBusinessArea(), bean.getPosition(), bean.getBeginDateAsLocalDate(),
                bean.getEndDateAsLocalDate(), bean.getApplicationType(), bean.getContractType(), bean.getSalary());
    }

}