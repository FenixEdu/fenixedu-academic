package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

public class PhdStudentReingressionRequest extends PhdStudentReingressionRequest_Base {

    private PhdStudentReingressionRequest() {
	super();
    }

    private PhdStudentReingressionRequest(final PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean) {
	super();

	init(academicServiceRequestCreateBean);
    }

    @Override
    protected void init(PhdAcademicServiceRequestCreateBean bean) {
	if (bean.getRequestType().equals(getAcademicServiceRequestType())) {
	    throw new DomainException("error.PhdStudentReingressionRequest.type.not.supported");
	}

	if (!isValidRequest(bean)) {
	    throw new PhdDomainOperationException(
		    "error.PhdStudentReingressionRequest.phd.individual.program.process.must.be.flunked.or.suspended");
	}

	super.init(bean);
    }

    private static boolean isValidRequest(final PhdAcademicServiceRequestCreateBean bean) {
	return bean.getPhdIndividualProgramProcess().isFlunked() || bean.getPhdIndividualProgramProcess().isSuspended();
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return AcademicServiceRequestType.PHD_STUDENT_REINGRESSION;
    }

    @Override
    public EventType getEventType() {
	return null;
    }

    @Override
    public Person getPerson() {
	return getPhdIndividualProgramProcess().getPerson();
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

    @Override
    public boolean isPayedUponCreation() {
	return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return false;
    }

    @Override
    public boolean isToPrint() {
	return false;
    }

    @Override
    public Boolean getFreeProcessed() {
	return true;
    }

    public static PhdStudentReingressionRequest createRequest(
	    final PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean) {
	return PhdStudentReingressionRequest.createRequest(academicServiceRequestCreateBean);
    }
}
