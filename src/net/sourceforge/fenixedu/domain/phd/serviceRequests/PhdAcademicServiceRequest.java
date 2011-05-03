package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.student.Student;


abstract public class PhdAcademicServiceRequest extends PhdAcademicServiceRequest_Base {

    public PhdAcademicServiceRequest() {
	super();
    }

    @Override
    protected void init(final AcademicServiceRequestCreateBean bean) {
	throw new DomainException("invoke init(PhdAcademicServiceRequestCreateBean)");
    }

    protected void init(final PhdAcademicServiceRequestCreateBean bean) {
	super.init(bean);
	checkParameters(bean);

	super.setPhdIndividualProgramProcess(bean.getPhdIndividualProgramProcess());
    }

    private void checkParameters(final PhdAcademicServiceRequestCreateBean bean) {
	if (bean.getPhdIndividualProgramProcess() == null) {
	    throw new DomainException("error.phd.serviceRequests.PhdAcademicServiceRequest.phdIndividualProgramProcess.is.null");
	}
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

}
