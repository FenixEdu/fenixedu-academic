package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

public class AdministrativeOfficeDocument extends FenixReport {
    
    private int LINE_LENGTH = 64;
    
    protected DomainReference<DocumentRequest> documentRequestDomainReference;

    public static class AdministrativeOfficeDocumentCreator {
	
	public static AdministrativeOfficeDocument create(final DocumentRequest documentRequest) {
	    switch (documentRequest.getDocumentRequestType()) {
	    case SCHOOL_REGISTRATION_DECLARATION:
		return new RegistrationDeclaration(documentRequest);
	    case ENROLMENT_CERTIFICATE:
		return new EnrolmentCertificate(documentRequest);
	    default:
		return new AdministrativeOfficeDocument(documentRequest);
	    }
	}
	
    }
    
    protected AdministrativeOfficeDocument() {
    }
    
    protected AdministrativeOfficeDocument(final DocumentRequest documentRequest) {
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
	parameters.put("name", StringUtils.multipleLineRightPad(LINE_LENGTH, name, '-'));
	
	final String documentIdNumber = person.getDocumentIdNumber();
	parameters.put("documentIdNumber", StringUtils.multipleLineRightPad(LINE_LENGTH, "portador do Bilhete de Identidade " + documentIdNumber, '-'));
	
	final String birthLocale = person.getParishOfBirth().toUpperCase() + ", " + person.getDistrictOfBirth().toUpperCase();
	parameters.put("birthLocale", StringUtils.multipleLineRightPad(LINE_LENGTH, "natural de " + birthLocale, '-'));
	
	final String nameOfFather = person.getNameOfFather().toUpperCase();
	final String sonOf = resourceBundle.getString("label.candidacy.registration.declaration.section4");
	parameters.put("nameOfFather", StringUtils.multipleLineRightPad(LINE_LENGTH, sonOf + " " + nameOfFather, '-'));
	
	final String nameOfMother = person.getNameOfMother().toUpperCase();
	final String andOf = resourceBundle.getString("label.candidacy.registration.declaration.section5");
	parameters.put("nameOfMother", StringUtils.multipleLineRightPad(LINE_LENGTH, andOf + " " + nameOfMother, '-'));
	
        if (getDocumentRequest().isPayable()) {
            final AdministrativeOfficeServiceAgreementTemplate serviceAgreementTemplate = getDocumentRequest().getAdministrativeOffice().getServiceAgreementTemplate();
            final PostingRule postingRule = serviceAgreementTemplate.findPostingRuleByEventType(getDocumentRequest().getEventType());
            parameters.put("postingRule", postingRule);
        }
    }
    
}
