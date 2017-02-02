package org.fenixedu.academic.service;

import org.fenixedu.academic.domain.student.Student;

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