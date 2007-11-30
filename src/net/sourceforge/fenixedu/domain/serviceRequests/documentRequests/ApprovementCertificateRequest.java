package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ApprovementCertificateRequest extends ApprovementCertificateRequest_Base {

    private ApprovementCertificateRequest() {
	super();
    }

    public ApprovementCertificateRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Boolean urgentRequest, MobilityProgram mobilityProgram) {

	this();

	this.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest, mobilityProgram);
    }

    final protected void init(final Registration registration,
	    final DocumentPurposeType documentPurposeType,
	    final String otherDocumentPurposeTypeDescription, final Boolean urgentRequest,
	    final MobilityProgram mobilityProgram) {

	super.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);
	checkParameters(mobilityProgram);
	super.setMobilityProgram(mobilityProgram);
    }

    final private void checkParameters(final MobilityProgram mobilityProgram) {
	if (mobilityProgram == null && getRegistration().hasAnyExternalApprovedEnrolment()) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.mobility.program.cannot.be.null.for.registration.with.external.enrolments");
	}
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);

	if (academicServiceRequestBean.isToProcess()) {

	    if (getRegistration().getCurriculum().isEmpty()) {
		throw new DomainException("ApprovementCertificateRequest.registration.without.approvements");
	    }

	    if (getRegistration().isConcluded()) {
		throw new DomainException("ApprovementCertificateRequest.registration.is.concluded");
	    }

	    // FIXME For now, the following conditions are only valid for 5year
	    // degrees
	    if (getRegistration().getDegreeType().getYears() == 5
		    && getDocumentPurposeType() == DocumentPurposeType.PROFESSIONAL) {

		int curricularYear = getRegistration().getCurricularYear();

		if (curricularYear <= 3) {
		    throw new DomainException("ApprovementCertificateRequest.registration.hasnt.finished.third.year");
		}
	    }
	}
    }
    
   @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.APPROVEMENT_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    final public EventType getEventType() {
	return EventType.APPROVEMENT_CERTIFICATE_REQUEST;
    }

    @Override
    final public Integer getNumberOfUnits() {
	return getRegistration().getCurriculum().getCurriculumEntries().size();
    }

}
