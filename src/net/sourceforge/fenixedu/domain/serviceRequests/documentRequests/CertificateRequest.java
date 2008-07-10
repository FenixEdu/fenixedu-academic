package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public abstract class CertificateRequest extends CertificateRequest_Base {

    protected CertificateRequest() {
	super();
	super.setNumberOfPages(0);
    }

    final protected void init(Registration registration, DateTime requestDate, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean freeProcessed) {
	init(registration, requestDate, null, freeProcessed, documentPurposeType, otherDocumentPurposeTypeDescription,
		urgentRequest);
    }

    final protected void init(Registration registration, DateTime requestDate, final ExecutionYear executionYear,
	    Boolean freeProcessed, DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Boolean urgentRequest) {

	super.init(registration, executionYear, requestDate, urgentRequest, freeProcessed);

	super.checkParameters(documentPurposeType, otherDocumentPurposeTypeDescription);
	super.setDocumentPurposeType(documentPurposeType);
	super.setOtherDocumentPurposeTypeDescription(otherDocumentPurposeTypeDescription);
    }

    static final public CertificateRequest create(final DocumentRequestCreateBean bean) {

	switch (bean.getChosenDocumentRequestType()) {
	case SCHOOL_REGISTRATION_CERTIFICATE:
	    return new SchoolRegistrationCertificateRequest(bean.getRegistration(), bean.getRequestDate(), bean
		    .getChosenDocumentPurposeType(), bean.getOtherPurpose(), bean.getUrgentRequest(), bean.getExecutionYear());

	case ENROLMENT_CERTIFICATE:
	    return new EnrolmentCertificateRequest(bean.getRegistration(), bean.getRequestDate(), bean.getChosenDocumentPurposeType(),
		    bean.getOtherPurpose(), bean.getUrgentRequest(), bean.getDetailed(), bean.getExecutionYear());

	case APPROVEMENT_CERTIFICATE:
	    return new ApprovementCertificateRequest(bean.getRegistration(), bean.getRequestDate(), bean.getChosenDocumentPurposeType(),
		    bean.getOtherPurpose(), bean.getUrgentRequest(), bean.getMobilityProgram());

	case DEGREE_FINALIZATION_CERTIFICATE:
	    return new DegreeFinalizationCertificateRequest(bean.getRegistration(), bean.getRequestDate(), bean
		    .getChosenDocumentPurposeType(), bean.getOtherPurpose(), bean.getUrgentRequest(), bean.getAverage(), bean
		    .getDetailed(), bean.getMobilityProgram(), bean.getRequestedCycle(), bean.getFreeProcessed(), bean
		    .getInternshipAbolished(), bean.getInternshipApproved(), bean.getStudyPlan(), bean
		    .getExceptionalConclusionDate());

	case EXAM_DATE_CERTIFICATE:
	    return new ExamDateCertificateRequest(bean.getRegistration(), bean.getRequestDate(), bean.getChosenDocumentPurposeType(),
		    bean.getOtherPurpose(), bean.getUrgentRequest(), bean.getEnrolments(), bean.getExams(), bean
			    .getExecutionPeriod());
	case COURSE_LOAD:
	    return new CourseLoadRequest(bean.getRegistration(), bean.getRequestDate(), ExecutionYear.readCurrentExecutionYear(), bean
		    .getChosenDocumentPurposeType(), bean.getOtherPurpose(), bean.getEnrolments(), bean.getUrgentRequest());

	case EXTERNAL_COURSE_LOAD:
	    return new ExternalCourseLoadRequest(bean.getRegistration(), bean.getRequestDate(), bean.getExecutionYear(), bean
		    .getChosenDocumentPurposeType(), bean.getOtherPurpose(), bean.getNumberOfCourseLoads(),
		    bean.getInstitution(), bean.getUrgentRequest());

	case PROGRAM_CERTIFICATE:
	    return new ProgramCertificateRequest(bean.getRegistration(), bean.getRequestDate(),
		    ExecutionYear.readCurrentExecutionYear(), bean.getChosenDocumentPurposeType(), bean.getOtherPurpose(), bean
			    .getEnrolments(), bean.getUrgentRequest());
	}

	return null;
    }

    @Override
    final public void setDocumentPurposeType(DocumentPurposeType documentPurposeType) {
	throw new DomainException("error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.documentPurposeType");
    }

    @Override
    final public void setOtherDocumentPurposeTypeDescription(String otherDocumentTypeDescription) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.otherDocumentTypeDescription");
    }

    abstract public Integer getNumberOfUnits();

    final public void edit(final DocumentRequestBean certificateRequestBean) {

	if (isPayable() && isPayed() && !getNumberOfPages().equals(certificateRequestBean.getNumberOfPages())) {
	    throw new DomainException("error.serviceRequests.documentRequests.cannot.change.numberOfPages.on.payed.documents");
	}

	super.edit(certificateRequestBean);
	super.setNumberOfPages(certificateRequestBean.getNumberOfPages());
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {

	super.internalChangeState(academicServiceRequestBean);

	if (academicServiceRequestBean.isToConclude()) {
	    tryConcludeServiceRequest(academicServiceRequestBean);
	}
    }

    protected void tryConcludeServiceRequest(final AcademicServiceRequestBean academicServiceRequestBean) {
	if (!hasNumberOfPages()) {
	    throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
	}

	if (!isFree()) {
	    new CertificateRequestEvent(getAdministrativeOffice(), getEventType(), getRegistration().getPerson(), this);
	}
    }

    /**
     * Important: Notice that this method's return value may not be the same
     * before and after conclusion of the academic service request.
     */
    @Override
    public boolean isFree() {
	if (getDocumentRequestType() == DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE
		|| getDocumentRequestType() == DocumentRequestType.ENROLMENT_CERTIFICATE) {
	    return super.isFree() || (!isRequestForPreviousExecutionYear() && isFirstRequestOfCurrentExecutionYear());
	}

	return super.isFree();
    }

    @Override
    public boolean isPayedUponCreation() {
	return false;
    }

    private boolean isRequestForPreviousExecutionYear() {
	return getExecutionYear() != ExecutionYear.readCurrentExecutionYear();
    }

    private boolean isFirstRequestOfCurrentExecutionYear() {
	return getRegistration().getSucessfullyFinishedDocumentRequestsBy(ExecutionYear.readCurrentExecutionYear(),
		getDocumentRequestType(), false).isEmpty();
    }

    @Override
    public boolean isPagedDocument() {
	return true;
    }

    @Override
    public boolean isToPrint() {
	return true;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return false;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return false;
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }

}
