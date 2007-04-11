package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

public abstract class DocumentRequest extends DocumentRequest_Base {

    protected DocumentRequest() {
	super();
    }

    final protected void checkParameters(final DocumentPurposeType documentPurposeType, final String otherDocumentPurposeTypeDescription) {
	if (documentPurposeType == DocumentPurposeType.OTHER && otherDocumentPurposeTypeDescription == null) {
	    throw new DomainException("error.serviceRequests.documentRequests.DocumentRequest.otherDocumentPurposeTypeDescription.cannot.be.null.for.other.purpose.type");
	}
    }

    @Override
    final public String getDescription() {
	return getDescription("AcademicServiceRequestType.DOCUMENT", getDocumentRequestType().getQualifiedName());
    }

    abstract public DocumentRequestType getDocumentRequestType();

    abstract public String getDocumentTemplateKey();

    final public boolean isCertificate() {
	return getDocumentRequestType().isCertificate();
    }

    final public boolean isDeclaration() {
	return getDocumentRequestType().isDeclaration();
    }

    final public boolean isDiploma() {
	return getDocumentRequestType().isDiploma();
    }

    final public boolean isPagedDocument() {
	return isCertificate() || isDeclaration();
    }

    final public static class DocumentRequestCreator extends DocumentRequestCreateBean implements
	    FactoryExecutor {

	public DocumentRequestCreator(Registration registration) {
	    super(registration);
	}

	public Object execute() {

	    if (getChosenDocumentRequestType().isCertificate()) {
		return CertificateRequest.create(getRegistration(), getChosenDocumentRequestType(),
			getChosenDocumentPurposeType(), getOtherPurpose(),
			getUrgentRequest(), getAverage(), getDetailed(), getExecutionYear());

	    } else if (getChosenDocumentRequestType().isDeclaration()) {
		return DeclarationRequest.create(getRegistration(), getChosenDocumentRequestType(),
			getChosenDocumentPurposeType(), getOtherPurpose(),
			getAverage(), getDetailed(), getYear(), getFreeProcessed());

	    } else if (getChosenDocumentRequestType().isDiploma()) {
		return new DiplomaRequest(getRegistration());

	    }
	    
	    return null;
	}

    }

    @Override
    protected void internalChangeState(AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {
	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.PROCESSING) {
	    if (getRegistration().hasGratuityDebtsCurrently() && !getFreeProcessed()) {
		throw new DomainException("DocumentRequest.registration.has.not.payed.gratuities");
	    }
	}
    }

    final public boolean isToBePrintedInAplica() {
	return !getRegistration().isBolonha() && getDocumentRequestType() == DocumentRequestType.APPROVEMENT_CERTIFICATE;
    }

}
