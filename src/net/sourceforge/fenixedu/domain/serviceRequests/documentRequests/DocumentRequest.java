package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.joda.time.DateTime;

public abstract class DocumentRequest extends DocumentRequest_Base {

    protected DocumentRequest() {
	super();
    }

    final protected void checkParameters(final DocumentPurposeType documentPurposeType,
	    final String otherDocumentPurposeTypeDescription) {
	if (documentPurposeType == DocumentPurposeType.OTHER && otherDocumentPurposeTypeDescription == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.DocumentRequest.otherDocumentPurposeTypeDescription.cannot.be.null.for.other.purpose.type");
	}
    }

    @Override
    public String getDescription() {
	return getDescription(getAcademicServiceRequestType(), getDocumentRequestType().getQualifiedName());
    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return AcademicServiceRequestType.DOCUMENT;
    }

    abstract public DocumentRequestType getDocumentRequestType();

    abstract public String getDocumentTemplateKey();

    abstract public boolean isPagedDocument();

    final public boolean isCertificate() {
	return getDocumentRequestType().isCertificate();
    }

    final public boolean isDeclaration() {
	return getDocumentRequestType().isDeclaration();
    }

    final public boolean isDiploma() {
	return getDocumentRequestType().isDiploma();
    }

    final public static class DocumentRequestCreator extends DocumentRequestCreateBean implements FactoryExecutor {

	public DocumentRequestCreator(Registration registration) {
	    super(registration);
	}

	public Object execute() {

	    if (getChosenDocumentRequestType().isCertificate()) {
		return CertificateRequest.create(getRegistration(), new DateTime(), getChosenDocumentRequestType(),
			getChosenDocumentPurposeType(), getOtherPurpose(), getUrgentRequest(), getAverage(), getDetailed(),
			getExecutionYear(), getMobilityProgram(), getRequestedCycle(), getFreeProcessed(), getEnrolments(),
			getExams(), getExecutionPeriod(), getInternship(), getStudyPlan());

	    } else if (getChosenDocumentRequestType().isDeclaration()) {
		return DeclarationRequest.create(getRegistration(), getChosenDocumentRequestType(),
			getChosenDocumentPurposeType(), getOtherPurpose(), getAverage(), getDetailed(), getYear(),
			getFreeProcessed());

	    } else if (getChosenDocumentRequestType().isDiploma()) {
		return new DiplomaRequest(getRegistration(), getRequestedCycle());

	    }

	    return null;
	}

    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);

	if (academicServiceRequestBean.isToProcess()) {
	    if (!getFreeProcessed() && getRegistration().hasGratuityDebtsCurrently()) {
		throw new DomainException("DocumentRequest.registration.has.not.payed.gratuities");
	    }
	}
    }

    final public boolean isToShowCredits() {
	return getDegreeType() != DegreeType.DEGREE;
    }

    public boolean hasNumberOfPages() {
	return getNumberOfPages() != null && getNumberOfPages().intValue() != 0;
    }

}
