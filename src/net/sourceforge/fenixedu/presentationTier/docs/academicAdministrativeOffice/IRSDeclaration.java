package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRSDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;

public class IRSDeclaration extends AdministrativeOfficeDocument {

    protected IRSDeclaration(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected DocumentRequest getDocumentRequest() {
        return (DocumentRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        //Employee loggedEmployee = AccessControl.getPerson().getEmployee();
        addParameter("documentRequest", getDocumentRequest());
        addParameter("documentTitle", getResourceBundle().getString("label.academicDocument.title.declaration"));
        String institutionName = getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName());
        String universityName = getMLSTextContent(UniversityUnit.getInstitutionsUniversityUnit().getPartyName());
        String socialSecurityNumber = RootDomainObject.getInstance().getInstitutionUnit().getSocialSecurityNumber().toString();
        addParameter("institutionName", institutionName);
        //addParameter("universityName", universityName);

        String stringTemplate1 = getResourceBundle().getString("label.academicDocument.irs.declaration.firstParagraph");
        addParameter(
                "firstParagraph",
                MessageFormat.format(stringTemplate1, institutionName.toUpperCase(getLocale()),
                        universityName.toUpperCase(getLocale())));

        String stringTemplate2 = getResourceBundle().getString("label.academicDocument.irs.declaration.secondParagraph");
        addParameter("secondParagraph", MessageFormat.format(stringTemplate2, socialSecurityNumber));
        addParameter("socialSecurityNumber", socialSecurityNumber);
        addParameter("thirdParagraph", getResourceBundle().getString("label.academicDocument.irs.declaration.thirdParagraph"));

        addParameter("sixthParagraph", getResourceBundle().getString("label.academicDocument.irs.declaration.sixthParagraph"));
        addParameter("seventhParagraph", getResourceBundle().getString("label.academicDocument.irs.declaration.seventhParagraph"));

        final Registration registration = getDocumentRequest().getRegistration();
        addParameter("registration", registration);

        final Person person = registration.getPerson();
        setPersonFields(registration, person);

        final Integer civilYear = ((IRSDeclarationRequest) getDocumentRequest()).getYear();
        addParameter("civilYear", civilYear.toString());

        setAmounts(person, civilYear);
        setEmployeeFields(institutionName);

        //addParameter("day", new YearMonthDay().toString(DD_MMMM_YYYY, getLocale()));
        setFooter(registration);
    }

    final private void setPersonFields(final Registration registration, final Person person) {
        final String name = person.getName().toUpperCase();
        addParameter("name", StringUtils.multipleLineRightPad(name, LINE_LENGTH, END_CHAR));

        final String registrationNumber = registration.getNumber().toString();
        String fourthParagraph = getResourceBundle().getString("label.academicDocument.irs.declaration.fourthParagraph");
        addParameter("fourthParagraph", fourthParagraph);
        int fourthParagraphLength = fourthParagraph.length();
        addParameter("registrationNumber",
                StringUtils.multipleLineRightPad(registrationNumber, LINE_LENGTH - fourthParagraphLength, END_CHAR));

        final String documentIdNumber = person.getDocumentIdNumber().toString();
        String fifthParagraph = getResourceBundle().getString("label.academicDocument.irs.declaration.fifthParagraph");
        addParameter("fifthParagraph", fifthParagraph);
        int fithParagraphLength = fifthParagraph.length();
        addParameter("documentIdNumber",
                StringUtils.multipleLineRightPad(documentIdNumber, LINE_LENGTH - fithParagraphLength, END_CHAR));
    }

    final private void setAmounts(final Person person, final Integer civilYear) {
        Money gratuityPayedAmount = person.getMaxDeductableAmountForLegalTaxes(EventType.GRATUITY, civilYear);
        Money othersPayedAmount = calculateOthersPayedAmount(person, civilYear);

        final StringBuilder eventTypes = new StringBuilder();
        final StringBuilder payedAmounts = new StringBuilder();
        if (!gratuityPayedAmount.isZero()) {
            eventTypes.append("- ").append(getEnumerationBundle().getString(EventType.GRATUITY.getQualifiedName()))
                    .append(LINE_BREAK);
            payedAmounts.append("*").append(gratuityPayedAmount.toPlainString()).append("Eur").append(LINE_BREAK);
        }

        if (!othersPayedAmount.isZero()) {
            eventTypes.append(getResourceBundle().getString("label.academicDocument.irs.declaration.eighthParagraph")).append(
                    LINE_BREAK);
            payedAmounts.append("*").append(othersPayedAmount.toPlainString()).append("Eur").append(LINE_BREAK);
        }
        addParameter("eventTypes", eventTypes.toString());
        addParameter("payedAmounts", payedAmounts.toString());

        Money totalPayedAmount = othersPayedAmount.add(gratuityPayedAmount);
        addParameter("totalPayedAmount", "*" + totalPayedAmount.toString() + "Eur");
        addParameter("total", getResourceBundle().getString("label.academicDocument.irs.declaration.total"));

    }

    private Money calculateOthersPayedAmount(final Person person, final Integer civilYear) {
        Money result = Money.ZERO;

        for (final EventType eventType : EventType.values()) {
            if (eventType != EventType.GRATUITY) {
                result = result.add(person.getMaxDeductableAmountForLegalTaxes(eventType, civilYear));
            }
        }

        return result;
    }

    final private void setEmployeeFields(String institutionName) {

        Unit adminOfficeUnit = getAdministrativeOffice().getUnit();
        Person coordinator = adminOfficeUnit.getActiveUnitCoordinator();
        String coordinatorTitle;
        if (coordinator.isMale()) {
            coordinatorTitle = getResourceBundle().getString("label.academicDocument.declaration.maleCoordinator");
        } else {
            coordinatorTitle = getResourceBundle().getString("label.academicDocument.declaration.femaleCoordinator");
        }

        String stringTemplate = getResourceBundle().getString("label.academicDocument.irs.declaration.signer");
        addParameter("signer",
                MessageFormat.format(stringTemplate, coordinatorTitle, getMLSTextContent(adminOfficeUnit.getPartyName())));

        addParameter("administrativeOfficeCoordinator", adminOfficeUnit.getActiveUnitCoordinator());
        String location = adminOfficeUnit.getCampus().getLocation();
        String dateDD = new LocalDate().toString("dd", getLocale());
        String dateMMMM = new LocalDate().toString("MMMM", getLocale());
        String dateYYYY = new LocalDate().toString("yyyy", getLocale());
        stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.signerLocation");
        addParameter("signerLocation",
                MessageFormat.format(stringTemplate, institutionName, location, dateDD, dateMMMM, dateYYYY));
        //addParameter("administrativeOfficeName", getMLSTextContent(adminOfficeUnit.getPartyName()));

        //addParameter("employeeLocation", adminOfficeUnit.getCampus().getLocation());

    }

    final private void setFooter(Registration registration) {
        String student;

        if (registration.getStudent().getPerson().isMale()) {
            student = getResourceBundle().getString("label.academicDocument.declaration.maleStudent");
        } else {
            student = getResourceBundle().getString("label.academicDocument.declaration.femaleStudent");
        }

        String stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.footer.studentNumber");
        addParameter("studentNumber", MessageFormat.format(stringTemplate, student, registration.getNumber().toString()));

        stringTemplate = getResourceBundle().getString("label.academicDocument.declaration.footer.documentNumber");
        addParameter("documentNumber",
                MessageFormat.format(stringTemplate, getDocumentRequest().getServiceRequestNumber().toString().trim()));
        addParameter("checked", getResourceBundle().getString("label.academicDocument.irs.declaration.checked"));

        addParameter("page", getResourceBundle().getString("label.academicDocument.declaration.footer.page"));
        addParameter("pageOf", getResourceBundle().getString("label.academicDocument.declaration.footer.pageOf"));
    }
}
