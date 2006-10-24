package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public class ApprovementCertificateRequest extends ApprovementCertificateRequest_Base {

    private ApprovementCertificateRequest() {
	super();
    }

    public ApprovementCertificateRequest(StudentCurricularPlan studentCurricularPlan,
	    AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest) {

	this();

	init(studentCurricularPlan, administrativeOffice, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
    }

    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();

	result.add(AdministrativeOfficeType.DEGREE);

	return result;
    }

    @Override
    public void conclude() throws DomainException {
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
    protected void internalChangeState(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {

	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED) {

	    new CertificateRequestEvent(getAdministrativeOffice(),
		    EventType.APPROVEMENT_CERTIFICATE_REQUEST, getStudent().getPerson(), this);
	}
    }

}
