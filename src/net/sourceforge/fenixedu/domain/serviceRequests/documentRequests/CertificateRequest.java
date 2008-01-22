package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;

public abstract class CertificateRequest extends CertificateRequest_Base {

    protected CertificateRequest() {
	super();
	super.setNumberOfPages(0);
    }

    final protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean freeProcessed) {
	init(registration, null, freeProcessed, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);
    }

    final protected void init(Registration registration, final ExecutionYear executionYear, Boolean freeProcessed,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription, Boolean urgentRequest) {

	super.init(registration, executionYear, urgentRequest, freeProcessed);

	super.checkParameters(documentPurposeType, otherDocumentPurposeTypeDescription);
	super.setDocumentPurposeType(documentPurposeType);
	super.setOtherDocumentPurposeTypeDescription(otherDocumentPurposeTypeDescription);
    }

    static final public CertificateRequest create(Registration registration, DocumentRequestType chosenDocumentRequestType,
	    DocumentPurposeType chosenDocumentPurposeType, String otherPurpose, Boolean urgentRequest, Boolean average,
	    Boolean detailed, ExecutionYear executionYear, MobilityProgram mobilityProgram, CycleType requestedCycle,
	    Boolean freeProcessed, Collection<Enrolment> enrolments, ExecutionPeriod executionPeriod) {

	switch (chosenDocumentRequestType) {
	case SCHOOL_REGISTRATION_CERTIFICATE:
	    return new SchoolRegistrationCertificateRequest(registration, chosenDocumentPurposeType, otherPurpose, urgentRequest,
		    executionYear);
	case ENROLMENT_CERTIFICATE:
	    return new EnrolmentCertificateRequest(registration, chosenDocumentPurposeType, otherPurpose, urgentRequest,
		    detailed, executionYear);
	case APPROVEMENT_CERTIFICATE:
	    return new ApprovementCertificateRequest(registration, chosenDocumentPurposeType, otherPurpose, urgentRequest,
		    mobilityProgram);
	case DEGREE_FINALIZATION_CERTIFICATE:
	    return new DegreeFinalizationCertificateRequest(registration, chosenDocumentPurposeType, otherPurpose, urgentRequest,
		    average, detailed, mobilityProgram, requestedCycle, freeProcessed);
	case EXAM_DATE_CERTIFICATE:
	    return new ExamDateCertificateRequest(registration, chosenDocumentPurposeType, otherPurpose, urgentRequest,
		    executionYear, enrolments, executionPeriod);
	case COURSE_LOAD:
	    return new CourseLoadRequest(registration, ExecutionYear.readCurrentExecutionYear(), chosenDocumentPurposeType,
		    otherPurpose, enrolments, urgentRequest);
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

	    if (!hasNumberOfPages()) {
		throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
	    }

	    if (!isFree()) {
		new CertificateRequestEvent(getAdministrativeOffice(), getEventType(), getRegistration().getPerson(), this);
	    }
	}
    }

    /**
     * Important: Notice that this method's return value may not be the same
     * before and after conclusion of the academic service request.
     */
    @Override
    final public boolean isFree() {
	if (getDocumentRequestType() == DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE
		|| getDocumentRequestType() == DocumentRequestType.ENROLMENT_CERTIFICATE) {
	    return super.isFree() || (!isRequestForPreviousExecutionYear() && isFirstRequestOfCurrentExecutionYear());
	}

	return super.isFree();
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
}
