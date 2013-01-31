package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class PhdAcademicServiceRequestCreateBean extends AcademicServiceRequestCreateBean implements IPhdAcademicServiceRequest {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private AcademicServiceRequestType requestType;

	private PhdIndividualProgramProcess phdIndividualProgramProcess;

	public PhdAcademicServiceRequestCreateBean(final PhdIndividualProgramProcess phdIndividualProgramProcess) {
		super(null);
		this.phdIndividualProgramProcess = phdIndividualProgramProcess;
	}

	@Override
	public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
		return this.phdIndividualProgramProcess;
	}

	public void setPhdIndividualProgramProcess(final PhdIndividualProgramProcess phdIndividualProgramProcess) {
		this.phdIndividualProgramProcess = phdIndividualProgramProcess;
	}

	public AcademicServiceRequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(final AcademicServiceRequestType requestType) {
		this.requestType = requestType;
	}

	@Override
	protected void setRegistration(final Registration registration) {
		// DO NOTHING
	}

	@Override
	public Registration getRegistration() {
		throw new DomainException("error.PhdAcademicServiceRequestCreateBean.get.registration.invalid");
	}

	@Service
	public PhdAcademicServiceRequest createNewRequest() {
		switch (getRequestType()) {
		case PHD_STUDENT_REINGRESSION:
			return PhdStudentReingressionRequest.createRequest(this);
		default:
			throw new DomainException("error.PhdAcademicServiceRequest.create.request.type.unknown");
		}
	}
}
