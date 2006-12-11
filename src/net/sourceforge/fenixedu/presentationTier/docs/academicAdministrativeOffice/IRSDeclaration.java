package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRSDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.YearMonthDay;

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
	
	final String name = person.getName().toUpperCase();
	parameters.put("name", StringUtils.multipleLineRightPad(name, LINE_LENGTH, '-'));

	final String registrationNumber = registration.getNumber().toString();
	parameters.put("registrationNumber", StringUtils.multipleLineRightPad(registrationNumber, LINE_LENGTH - "aluno deste Instituto com o Número ".length(), '-'));
	
	final String documentIdNumber = person.getDocumentIdNumber();
	parameters.put("documentIdNumber", StringUtils.multipleLineRightPad(documentIdNumber, LINE_LENGTH - "portador do Bilhete de Identidade ".length(), '-'));
	
	final Integer civilYear = IRSDeclarationRequest.getYear();
	parameters.put("civilYear", civilYear.toString());

	Money gratuityPayedAmount = person.getPayedAmount(EventType.GRATUITY, civilYear);
	Money officeFeeAndInsurancePayedAmount = person.getPayedAmount(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, civilYear);
	Money othersPayedAmount = Money.ZERO;
	for (final EventType eventType : EventType.values()) {
	    if (eventType != EventType.GRATUITY && eventType != EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE) {
		othersPayedAmount = othersPayedAmount.add(person.getPayedAmount(eventType, civilYear));
	    }
	}

	final StringBuilder eventTypes = new StringBuilder();
	final StringBuilder payedAmounts = new StringBuilder();
	if (!gratuityPayedAmount.isZero()) {
	    eventTypes.append("- ").append(enumerationBundle.getString(EventType.GRATUITY.getQualifiedName())).append("\n");
	    payedAmounts.append("*").append(gratuityPayedAmount.toPlainString()).append("Eur").append("\n");
	}
	if (!officeFeeAndInsurancePayedAmount.isZero()) {
	    eventTypes.append("- ").append(enumerationBundle.getString(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE.getQualifiedName())).append("\n");
	    payedAmounts.append("*").append(officeFeeAndInsurancePayedAmount.toPlainString()).append("Eur").append("\n");
	}
	if (!othersPayedAmount.isZero()) {
	    eventTypes.append("- Diversos").append("\n");
	    payedAmounts.append("*").append(othersPayedAmount.toPlainString()).append("Eur").append("\n");
	}
	
	parameters.put("eventTypes", eventTypes.toString());
	parameters.put("payedAmounts", payedAmounts.toString());

	Money totalPayedAmount = othersPayedAmount.add(gratuityPayedAmount).add(officeFeeAndInsurancePayedAmount);
	parameters.put("totalPayedAmount", "*" + totalPayedAmount.toString() + "Eur");
	
	parameters.put("employeeLocation", AccessControl.getPerson().getEmployee().getCurrentCampus().getLocation());
	parameters.put("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
    }

}
