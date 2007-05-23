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

public class DegreeFinalizationCertificateRequest extends DegreeFinalizationCertificateRequest_Base {

    protected DegreeFinalizationCertificateRequest() {
	super();
    }

    public DegreeFinalizationCertificateRequest(final Registration registration,
	    final DocumentPurposeType documentPurposeType, final String otherDocumentPurposeTypeDescription,
	    final Boolean urgentRequest, final Boolean average, final Boolean detailed) {
	this();

	this.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		average, detailed);
    }

    final protected void init(final Registration registration, final DocumentPurposeType documentPurposeType,
	    final String otherDocumentPurposeTypeDescription, final Boolean urgentRequest, final Boolean average,
	    final Boolean detailed) {
	super.init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

	checkParameters(average, detailed);
	super.setAverage(average);
	super.setDetailed(detailed);
    }

    final private void checkParameters(Boolean average, Boolean detailed) {
	if (average == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.average.cannot.be.null");
	}
	if (detailed == null) {
	    throw new DomainException("DegreeFinalizationCertificateRequest.detailed.cannot.be.null");
	}
    }

    @Override
    final public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();

	result.add(AdministrativeOfficeType.DEGREE);

	return result;
    }

    @Override
    final protected void internalChangeState(final AcademicServiceRequestSituationType academicServiceRequestSituationType, final Employee employee) {
	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.PROCESSING) {
	    if (!getRegistration().isConcluded()) {
		throw new DomainException("DegreeFinalizationCertificateRequest.registration.is.not.concluded");
	    } else if (!getRegistration().hasDiplomaRequest()) {
		throw new DomainException("DegreeFinalizationCertificateRequest.registration.withoutDiplomaRequest");
	    }
	}
    }
    
    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    final public void setAverage(final Boolean average) {
	throw new DomainException("DegreeFinalizationCertificateRequest.cannot.modify.average");
    }

    @Override
    final public void setDetailed(final Boolean detailed) {
	throw new DomainException("DegreeFinalizationCertificateRequest.cannot.modify.detailed");
    }

    @Override
    final public EventType getEventType() {
	return EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST;
    }

    @Override
    final public ExecutionYear getExecutionYear() {
	return null;
    }

    @Override
    final public Integer getNumberOfUnits() {
	return getDetailed() ? getRegistration().getApprovedEnrolments().size() : 0;
    }

}
