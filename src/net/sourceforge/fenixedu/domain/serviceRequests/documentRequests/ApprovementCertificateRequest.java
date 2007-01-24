package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ApprovementCertificateRequest extends ApprovementCertificateRequest_Base {

    private ApprovementCertificateRequest() {
	super();
    }

    public ApprovementCertificateRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Boolean urgentRequest) {

	this();

	init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);
    }

    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();

	result.add(AdministrativeOfficeType.DEGREE);

	return result;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {
	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.PROCESSING) {
	    if (!getRegistration().hasAnyApprovedEnrolment()) {
		throw new DomainException("ApprovementCertificateRequest.registration.without.approvements");
	    }

	    if (getRegistration().isConcluded()) {
		throw new DomainException("ApprovementCertificateRequest.registration.is.concluded");
	    }

	    // FIXME For now, the following conditions are only valid for 5 year
	    // degrees
	    if (getRegistration().getDegreeType().getYears() == 5
		    && getDocumentPurposeType() == DocumentPurposeType.PROFESSIONAL) {

		int curricularYear = getRegistration().getCurricularYear();

		if (curricularYear <= 3) {
		    throw new DomainException(
			    "ApprovementCertificateRequest.registration.hasnt.finished.third.year");
		}
	    }
	}
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.APPROVEMENT_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public EventType getEventType() {
	return EventType.APPROVEMENT_CERTIFICATE_REQUEST;
    }

    @Override
    public ExecutionYear getExecutionYear() {
	return null;
    }

    @Override
    public Integer getNumberOfUnits() {
	return getRegistration().getApprovedEnrolments().size();
    }

}
