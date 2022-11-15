package org.fenixedu.academic.domain;

import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

public class Employee extends Employee_Base {

    public Employee() {
        super();
        setRoot(Bennu.getInstance());
    }

    @Override
    public void setNumber(String number) {
        if (findByNumber(number).filter(e -> e != this).isPresent()) {
            throw new IllegalStateException("Employee already exists with same number");
        }

        super.setNumber(number);
    }

    public static Stream<Employee> findAll() {
        return Bennu.getInstance().getEmployeesSet().stream();
    }

    public static Optional<Employee> findByNumber(String number) {
        return StringUtils.isBlank(number) ? Optional.empty() : findAll().filter(e -> number.equals(e.getNumber())).findAny();
    }

    protected void delete() {
        setRoot(null);
        setPerson(null);
        super.deleteDomainObject();
    }

}
