package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
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
	addParameter("documentRequest", getDocumentRequest());

	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());
	
	final Registration registration = getDocumentRequest().getRegistration();
	addParameter("registration", registration);
	
	final Person person = registration.getPerson();
	setPersonFields(registration, person);
	
	final Integer civilYear = ((IRSDeclarationRequest) getDocumentRequest()).getYear();
	addParameter("civilYear", civilYear.toString());

	setAmounts(person, civilYear);
        setEmployeeFields();

        addParameter("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
    }

    final private void setPersonFields(final Registration registration, final Person person) {
	final String name = person.getName().toUpperCase();
	addParameter("name", StringUtils.multipleLineRightPad(name, LINE_LENGTH, '-'));

	final String registrationNumber = registration.getNumber().toString();
	addParameter("registrationNumber", StringUtils.multipleLineRightPad(registrationNumber, LINE_LENGTH - "aluno deste Instituto com o Número ".length(), '-'));
	
	final StringBuilder documentIdType = new StringBuilder();
	documentIdType.append("portador" + (person.isMale() ? "" : "a"));
	documentIdType.append(" do ");
	documentIdType.append(person.getIdDocumentType().getLocalizedName());
	documentIdType.append(" Nº ");
	addParameter("documentIdType", documentIdType.toString());

	final String documentIdNumber = person.getDocumentIdNumber();
	addParameter("documentIdNumber", StringUtils.multipleLineRightPad(documentIdNumber, LINE_LENGTH - documentIdType.toString().length(), '-'));
    }

    final private void setAmounts(final Person person, final Integer civilYear) {
	Money gratuityPayedAmount = person.getMaxDeductableAmountForLegalTaxes(EventType.GRATUITY, civilYear);
	Money officeFeeAndInsurancePayedAmount = person.getMaxDeductableAmountForLegalTaxes(EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, civilYear);
	Money othersPayedAmount = Money.ZERO;
	for (final EventType eventType : EventType.values()) {
	    if (eventType != EventType.GRATUITY && eventType != EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE) {
		othersPayedAmount = othersPayedAmount.add(person.getMaxDeductableAmountForLegalTaxes(eventType, civilYear));
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
	
	addParameter("eventTypes", eventTypes.toString());
	addParameter("payedAmounts", payedAmounts.toString());

	Money totalPayedAmount = othersPayedAmount.add(gratuityPayedAmount).add(officeFeeAndInsurancePayedAmount);
	addParameter("totalPayedAmount", "*" + totalPayedAmount.toString() + "Eur");
    }
    
    final private void setEmployeeFields() {
	final Employee employee = AccessControl.getPerson().getEmployee();
	
	addParameter("administrativeOfficeCoordinator", employee.getCurrentWorkingPlace().getActiveUnitCoordinator());
	addParameter("administrativeOfficeName", employee.getCurrentWorkingPlace().getName());
	addParameter("employeeLocation", AccessControl.getPerson().getEmployee().getCurrentCampus().getLocation());
    }

}
