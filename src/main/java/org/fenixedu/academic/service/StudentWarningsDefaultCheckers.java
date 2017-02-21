package org.fenixedu.academic.service;

import org.fenixedu.academic.domain.IdentificationDocumentExtraDigit;
import org.fenixedu.academic.domain.IdentificationDocumentSeriesNumber;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class StudentWarningsDefaultCheckers {

    public final static Function<Student, Collection<String>> WARNING_VALID_ID_DOCUMENT = student -> {
        Collection<String> warnings = new ArrayList<>();
        Person person = student.getPerson();
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
        return warnings;
    };
}
