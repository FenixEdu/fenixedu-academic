package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.List;
import java.util.Locale;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.documents.DocumentRequestGeneratedDocument;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.AdministrativeOfficeDocument;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.joda.time.DateTime;

public abstract class DocumentRequest extends DocumentRequest_Base {

    protected DocumentRequest() {
	super();
    }

    protected void checkParameters(final DocumentRequestCreateBean bean) {
	if (bean.getChosenDocumentPurposeType() == DocumentPurposeType.OTHER && bean.getOtherPurpose() == null) {
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

    public boolean isRegistryDiploma() {
	return getDocumentRequestType().isRegistryDiploma();
    }

    final public boolean isDiplomaSupplement() {
	return getDocumentRequestType().isDiplomaSupplement();
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);

	if (academicServiceRequestBean.isToProcess()) {
	    if (!getFreeProcessed()) {
		assertPayedEvents();
	    }
	}
    }

    protected void assertPayedEvents() {
	if (getRegistration().hasGratuityDebtsCurrently()) {
	    throw new DomainException("DocumentRequest.registration.has.not.payed.gratuities");
	}

	if (getRegistration().hasInsuranceDebtsCurrently()) {
	    throw new DomainException("DocumentRequest.registration.has.not.payed.insurance.fees");
	}

	if (getRegistration().hasAdministrativeOfficeFeeAndInsuranceDebtsCurrently(getAdministrativeOffice())) {
	    throw new DomainException("DocumentRequest.registration.has.not.payed.administrative.office.fees");
	}
    }

    protected void assertPayedEvents(final ExecutionYear executionYear) {
	if (executionYear != null) {
	    if (getRegistration().hasGratuityDebts(executionYear)) {
		throw new DomainException("DocumentRequest.registration.has.not.payed.gratuities");
	    }

	    if (getRegistration().hasInsuranceDebts(executionYear)) {
		throw new DomainException("DocumentRequest.registration.has.not.payed.insurance.fees");
	    }

	    if (getRegistration().hasAdministrativeOfficeFeeAndInsuranceDebts(getAdministrativeOffice(), executionYear)) {
		throw new DomainException("DocumentRequest.registration.has.not.payed.administrative.office.fees");
	    }
	}
    }

    final public boolean isToShowCredits() {
	return getDegreeType() != DegreeType.DEGREE;
    }

    @Override
    public boolean isRequestAvailableToSendToExternalEntity() {
	return super.isRequestAvailableToSendToExternalEntity() && !hasRectorateSubmissionBatch();
    }

    public boolean hasNumberOfPages() {
	return getNumberOfPages() != null && getNumberOfPages().intValue() != 0;
    }

    public Locale getLocale() {
	return null;
    }

    @Override
    protected void checkRulesToDelete() {
	super.checkRulesToDelete();
	if (hasRegistryCode()) {
	    throw new DomainException("error.AcademicServiceRequest.cannot.be.deleted");
	}
    }

    protected void generateDocument() {
	try {
	    final List<AdministrativeOfficeDocument> documents = (List<AdministrativeOfficeDocument>) AdministrativeOfficeDocument.AdministrativeOfficeDocumentCreator
		    .create(this);
	    final AdministrativeOfficeDocument[] array = {};
	    byte[] data = ReportsUtils.exportMultipleToPdfAsByteArray(documents.toArray(array));
	    DocumentRequestGeneratedDocument.store(this, documents.iterator().next().getReportFileName() + ".pdf", data);
	} catch (JRException e) {
	    throw new DomainException("error.documentRequest.errorGeneratingDocument");
	}
    }

    public GeneratedDocument getLastGeneratedDocument() {
	DateTime last = null;
	GeneratedDocument lastDoc = null;
	for (GeneratedDocument document : getDocumentSet()) {
	    if (last == null || document.getUploadTime().isAfter(last)) {
		last = document.getUploadTime();
		lastDoc = document;
	    }
	}
	return lastDoc;
    }
}
