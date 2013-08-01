package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRSDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;

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
        final Registration registration = getDocumentRequest().getRegistration();
        final Person person = registration.getPerson();
        final Integer civilYear = ((IRSDeclarationRequest) getDocumentRequest()).getYear();

        addParameter("registration", registration);
        addParameter("documentTitle", getResourceBundle().getString("label.academicDocument.title.declaration"));
        String institutionName = getInstitutionName();
        String universityName = getUniversityName(new DateTime());
        String socialSecurityNumber = RootDomainObject.getInstance().getInstitutionUnit().getSocialSecurityNumber().toString();

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

        setPersonFields(registration, person);

        addParameter("civilYear", civilYear.toString());

        setAmounts(person, civilYear);
        setFooter(getDocumentRequest());
        fillEmployeeFields();
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

}
