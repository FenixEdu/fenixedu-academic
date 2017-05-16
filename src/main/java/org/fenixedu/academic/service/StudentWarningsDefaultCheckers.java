package org.fenixedu.academic.service;

import java.text.Collator;
import java.util.Collection;
import java.util.TreeSet;
import java.util.function.Function;

import org.fenixedu.academic.domain.IdentificationDocumentExtraDigit;
import org.fenixedu.academic.domain.IdentificationDocumentSeriesNumber;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.YearMonthDay;

public class StudentWarningsDefaultCheckers {

    public final static Function<Student, Collection<String>> WARNING_VALID_ID_DOCUMENT = student -> {
        final Collection<String> warnings = new TreeSet<String>();
        final Person person = student.getPerson();
        if (person.getIdDocumentType() == IDDocumentType.IDENTITY_CARD) {
            String identificationDocumentSeriesNumber = person.getIdentificationDocumentSeriesNumber();
            if (identificationDocumentSeriesNumber.length() == 1) {
                try {
                    IdentificationDocumentExtraDigit
                            .validate(person.getDocumentIdNumber(), identificationDocumentSeriesNumber);
                } catch (DomainException de) {
                    warnings.add(BundleUtil.getString(Bundle.APPLICATION, de.getMessage()));
                }
            } else {
                try {
                    IdentificationDocumentSeriesNumber
                            .validate(person.getDocumentIdNumber(), identificationDocumentSeriesNumber);
                } catch (DomainException de) {
                    warnings.add(BundleUtil.getString(Bundle.APPLICATION, de.getMessage()));
                }
            }
        }
        final YearMonthDay documentExpirationDate = person.getExpirationDateOfDocumentIdYearMonthDay();
        if (documentExpirationDate == null) {
            warnings.add(BundleUtil.getString(Bundle.APPLICATION, "label.identificationDocument.no.expiration.date"));
        } else if (documentExpirationDate.isBefore(new YearMonthDay())) {
            warnings.add(BundleUtil.getString(Bundle.APPLICATION, "label.identificationDocument.past.expiration.date"));
        }
        return warnings;
    };
}
