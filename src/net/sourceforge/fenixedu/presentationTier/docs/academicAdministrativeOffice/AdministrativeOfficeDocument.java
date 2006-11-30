package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

public class AdministrativeOfficeDocument extends FenixReport {
    
    protected int LINE_LENGTH = 64;
    
    protected DomainReference<DocumentRequest> documentRequestDomainReference;

    public static class AdministrativeOfficeDocumentCreator {
	
	public static AdministrativeOfficeDocument create(final DocumentRequest documentRequest) {
	    switch (documentRequest.getDocumentRequestType()) {
	    case ENROLMENT_CERTIFICATE:
		return new EnrolmentCertificate(documentRequest);
	    case APPROVEMENT_CERTIFICATE:
		return new ApprovementCertificate(documentRequest);
	    case DEGREE_FINALIZATION_CERTIFICATE:
		return new DegreeFinalizationCertificate(documentRequest);
	    case SCHOOL_REGISTRATION_DECLARATION:
		return new RegistrationDeclaration(documentRequest);
	    default:
		return new AdministrativeOfficeDocument(documentRequest);
	    }
	}
	
    }
    
    protected AdministrativeOfficeDocument() {
    }
    
    protected AdministrativeOfficeDocument(final DocumentRequest documentRequest) {
	this.dataSource = new ArrayList();
	this.resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", LanguageUtils.getLocale());
	this.documentRequestDomainReference = new DomainReference<DocumentRequest>(documentRequest);
	
	fillReport();
    }

    protected DocumentRequest getDocumentRequest() {
	return documentRequestDomainReference.getObject();
    }
    
    @Override
    public String getReportTemplateKey() {
	return getDocumentRequest().getDocumentTemplateKey();
    }
    
    @Override
    public String getReportFileName() {
	final StringBuilder result = new StringBuilder();

	result.append(getDocumentRequest().getRegistration().getPerson().getIstUsername());
	result.append("-");
	result.append(new DateTime().toString(DateTimeFormat.forPattern("yyyyMMdd")));
	result.append("-");
	result.append(getDocumentRequest().getDescription().replace(":", "").replace(" ", ""));

	return result.toString();
    }

    @Override
    protected void fillReport() {
	parameters.put("documentRequest", getDocumentRequest());
	parameters.put("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));

	final Person person = getDocumentRequest().getRegistration().getPerson();
	final String name = person.getName().toUpperCase();
	parameters.put("name", StringUtils.multipleLineRightPad(name, LINE_LENGTH, '-'));
	
	final String documentIdNumber = person.getDocumentIdNumber();
	parameters.put("documentIdNumber", StringUtils.multipleLineRightPad("portador do Bilhete de Identidade " + documentIdNumber, LINE_LENGTH, '-'));
	
	final String birthLocale = person.getParishOfBirth().toUpperCase() + ", " + person.getDistrictOfBirth().toUpperCase();
	parameters.put("birthLocale", StringUtils.multipleLineRightPad("natural de " + birthLocale, LINE_LENGTH, '-'));
	
	final String nameOfFather = person.getNameOfFather().toUpperCase();
	final String sonOf = resourceBundle.getString("label.candidacy.registration.declaration.section4");
	parameters.put("nameOfFather", StringUtils.multipleLineRightPad(sonOf + " " + nameOfFather, LINE_LENGTH, '-'));
	
	final String nameOfMother = person.getNameOfMother().toUpperCase();
	final String andOf = resourceBundle.getString("label.candidacy.registration.declaration.section5");
	parameters.put("nameOfMother", StringUtils.multipleLineRightPad(andOf + " " + nameOfMother, LINE_LENGTH, '-'));
	
        if (getDocumentRequest().isCertificate()) {
            final CertificateRequestPR certificateRequestPR = (CertificateRequestPR) getPostingRule();
            
            final Money amountPerPage = certificateRequestPR.getAmountPerPage();
            final Money baseAmount = certificateRequestPR.getBaseAmount();
            final Money urgencyAmount = ((CertificateRequest)getDocumentRequest()).getUrgentRequest() ? certificateRequestPR.getBaseAmount() : Money.ZERO;
            final Money totalAmount = amountPerPage.add(baseAmount).add(urgencyAmount);
            
            parameters.put("amountPerPage", amountPerPage);
            parameters.put("baseAmount", baseAmount);
            parameters.put("urgencyAmount", urgencyAmount);
            parameters.put("totalAmount", totalAmount);
        }
    }
    
    protected final PostingRule getPostingRule() {
	final AdministrativeOfficeServiceAgreementTemplate serviceAgreementTemplate = getDocumentRequest().getAdministrativeOffice().getServiceAgreementTemplate();
	return serviceAgreementTemplate.findPostingRuleByEventType(getDocumentRequest().getEventType());
    }
    
}
