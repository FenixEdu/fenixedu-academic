package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Student;

abstract public class PhdAcademicServiceRequest extends PhdAcademicServiceRequest_Base {

    public PhdAcademicServiceRequest() {
        super();
    }

    protected void init(final PhdAcademicServiceRequestCreateBean bean) {
        checkParameters(bean);

        super.setPhdIndividualProgramProcess(bean.getPhdIndividualProgramProcess());
        super.init(bean, getPhdIndividualProgramProcess().getPhdProgram().getAdministrativeOffice());
    }

    private void checkParameters(final PhdAcademicServiceRequestCreateBean bean) {
        if (bean.getPhdIndividualProgramProcess() == null) {
            throw new DomainException("error.phd.serviceRequests.PhdAcademicServiceRequest.phdIndividualProgramProcess.is.null");
        }
    }

    @Override
    public AcademicProgram getAcademicProgram() {
        return getPhdIndividualProgramProcess().getPhdProgram();
    }

    @Override
    public void setPhdIndividualProgramProcess(PhdIndividualProgramProcess phdIndividualProgramProcess) {
        throw new DomainException("error.phd.serviceRequests.PhdAcademicServiceRequest.phdIndividualProgramProcess.is.immutable");
    }

    @Override
    public boolean isRequestForPhd() {
        return true;
    }

    @Override
    public Person getPerson() {
        return getPhdIndividualProgramProcess().getPerson();
    }

    public Student getStudent() {
        return getPhdIndividualProgramProcess().getStudent();
    }

    public Campus getCampus() {
        return Campus.readActiveCampusByName("Alameda");
    }
}
