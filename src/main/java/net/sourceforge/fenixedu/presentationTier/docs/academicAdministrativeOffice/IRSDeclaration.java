/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IRSDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.FenixStringTools;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
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
        addParameter("documentTitle", BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.title.declaration"));
        String institutionName = getInstitutionName();
        String universityName = getUniversityName(new DateTime());
        String socialSecurityNumber = Bennu.getInstance().getInstitutionUnit().getSocialSecurityNumber().toString();

        String stringTemplate1 = BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.irs.declaration.firstParagraph");
        addParameter(
                "firstParagraph",
                MessageFormat.format(stringTemplate1, institutionName.toUpperCase(getLocale()),
                        universityName.toUpperCase(getLocale())));

        String stringTemplate2 = BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.irs.declaration.secondParagraph");
        addParameter("secondParagraph", MessageFormat.format(stringTemplate2, socialSecurityNumber));
        addParameter("socialSecurityNumber", socialSecurityNumber);
        addParameter("thirdParagraph", BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.irs.declaration.thirdParagraph"));

        addParameter("sixthParagraph", BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.irs.declaration.sixthParagraph"));
        addParameter("seventhParagraph", BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.irs.declaration.seventhParagraph"));

        setPersonFields(registration, person);

        addParameter("civilYear", civilYear.toString());

        setAmounts(person, civilYear);
        setFooter(getDocumentRequest());
        fillEmployeeFields();
    }

    final private void setPersonFields(final Registration registration, final Person person) {
        final String name = person.getName().toUpperCase();
        addParameter("name", FenixStringTools.multipleLineRightPad(name, LINE_LENGTH, END_CHAR));

        final String registrationNumber = registration.getNumber().toString();
        String fourthParagraph = BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.irs.declaration.fourthParagraph");
        addParameter("fourthParagraph", fourthParagraph);
        int fourthParagraphLength = fourthParagraph.length();
        addParameter("registrationNumber",
                FenixStringTools.multipleLineRightPad(registrationNumber, LINE_LENGTH - fourthParagraphLength, END_CHAR));

        final String documentIdNumber = person.getDocumentIdNumber().toString();
        String fifthParagraph = BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.irs.declaration.fifthParagraph");
        addParameter("fifthParagraph", fifthParagraph);
        int fithParagraphLength = fifthParagraph.length();
        addParameter("documentIdNumber",
                FenixStringTools.multipleLineRightPad(documentIdNumber, LINE_LENGTH - fithParagraphLength, END_CHAR));
    }

    final private void setAmounts(final Person person, final Integer civilYear) {
        Money gratuityPayedAmount = person.getMaxDeductableAmountForLegalTaxes(EventType.GRATUITY, civilYear);
        Money othersPayedAmount = calculateOthersPayedAmount(person, civilYear);

        final StringBuilder eventTypes = new StringBuilder();
        final StringBuilder payedAmounts = new StringBuilder();
        if (!gratuityPayedAmount.isZero()) {
            eventTypes.append("- ").append(BundleUtil.getString(Bundle.ENUMERATION, EventType.GRATUITY.getQualifiedName()))
                    .append(LINE_BREAK);
            payedAmounts.append("*").append(gratuityPayedAmount.toPlainString()).append("Eur").append(LINE_BREAK);
        }

        if (!othersPayedAmount.isZero()) {
            eventTypes.append(BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.irs.declaration.eighthParagraph")).append(
                    LINE_BREAK);
            payedAmounts.append("*").append(othersPayedAmount.toPlainString()).append("Eur").append(LINE_BREAK);
        }
        addParameter("eventTypes", eventTypes.toString());
        addParameter("payedAmounts", payedAmounts.toString());

        Money totalPayedAmount = othersPayedAmount.add(gratuityPayedAmount);
        addParameter("totalPayedAmount", "*" + totalPayedAmount.toString() + "Eur");
        addParameter("total", BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.irs.declaration.total"));

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
