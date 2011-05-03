package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;

import org.joda.time.DateTime;

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
	if (!bean.getRequestType().equals(getAcademicServiceRequestType())) {
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
    public boolean isManagedWithRectorateSubmissionBatch() {
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

    public static PhdAcademicServiceRequest createRequest(
	    final PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean) {
	return new PhdStudentReingressionRequest(academicServiceRequestCreateBean);
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);

	ResourceBundle phdBundle = ResourceBundle.getBundle("resources.PhdResources");

	if (academicServiceRequestBean.isToConclude()) {
	    PhdIndividualProgramProcess process = getPhdIndividualProgramProcess();
	    PhdProgramProcessState lastActiveState = process.getLastActiveState();
	    String remarks = String
		    .format(
			    phdBundle
				    .getString("message.net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdStudentReingressionRequest.conclusion.remark"),
			    getServiceRequestNumberYear());

	    process.createState(lastActiveState.getType(), getEmployee().getPerson(), remarks);

	    if (process.hasRegistration() && !process.getRegistration().isActive()) {
		RegistrationState registrationLastActiveState = process.getRegistration().getLastActiveState();

		RegistrationStateCreator.createState(process.getRegistration(), getEmployee().getPerson(), new DateTime(),
			registrationLastActiveState.getStateType());
	    }

	}
    }
}
