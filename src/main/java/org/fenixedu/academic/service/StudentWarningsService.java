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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentWarningsService {
    private static List<Function<Student, Collection<String>>> checkers = new CopyOnWriteArrayList<>();

    public final static Function<Student, Collection<String>> WARNING_VALID_ID_DOCUMENT = student -> {
        Collection<String> warnings = new ArrayList<>();
        Person person = student.getPerson();
        if (person.getIdDocumentType().equals(IDDocumentType.IDENTITY_CARD)) {
            if (person.getIdentificationDocumentSeriesNumber().length() == 1) {
                try {
                    IdentificationDocumentExtraDigit
                            .validate(person.getDocumentIdNumber(), person.getIdentificationDocumentSeriesNumber());
                } catch (DomainException de) {
                    warnings.add(BundleUtil.getString(Bundle.APPLICATION, de.getMessage()));
                }
            } else {
                try {
                    IdentificationDocumentSeriesNumber
                            .validate(person.getDocumentIdNumber(), person.getIdentificationDocumentSeriesNumber());
                } catch (DomainException de) {
                    warnings.add(BundleUtil.getString(Bundle.APPLICATION, de.getMessage()));
                }
            }
        }
        return warnings;
    };

    public static boolean register(Function<Student, Collection<String>> checker) {
        return checkers.add(checker);
    }
    
    public static Collection<String> check(Student student) {
        return checkers.stream()
                .flatMap(f -> Optional.ofNullable(f.apply(student)).map(c -> c.stream()).orElse(Stream.empty()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}