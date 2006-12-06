package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRSDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StringUtils;

public class IRSDeclaration extends AdministrativeOfficeDocument {

    protected IRSDeclaration(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	final IRSDeclarationRequest IRSDeclarationRequest = (IRSDeclarationRequest) getDocumentRequest();
	final Registration registration = IRSDeclarationRequest.getRegistration();
	final Person person = registration.getPerson();
	
	parameters.put("registration", registration);
	//parameters.put("address", person.getAddress().toUpperCase());
	//parameters.put("postalCode", person.getPostalCode().toUpperCase());
	
	final String name = person.getName().toUpperCase();
	parameters.put("name", StringUtils.multipleLineRightPad(name, LINE_LENGTH, '-'));

	final String registrationNumber = registration.getNumber().toString();
	parameters.put("registrationNumber", StringUtils.multipleLineRightPad(registrationNumber, LINE_LENGTH - "aluno deste Instituto com o Número ".length(), '-'));
	
	final String documentIdNumber = person.getDocumentIdNumber();
	parameters.put("documentIdNumber", StringUtils.multipleLineRightPad(documentIdNumber, LINE_LENGTH - "portador do Bilhete de Identidade ".length(), '-'));
	
	final Integer civilYear = IRSDeclarationRequest.getYear();
	parameters.put("civilYear", civilYear.toString());

	parameters.put("gratuityEventsPayedAmount", person.getGratuityEventsPayedAmount(civilYear));
	parameters.put("administrativeOfficeFeeAndInsuranceEventsPayedAmount", person.getAdministrativeOfficeFeeAndInsuranceEventsPayedAmount(civilYear));
	
	parameters.put("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
    }

}
