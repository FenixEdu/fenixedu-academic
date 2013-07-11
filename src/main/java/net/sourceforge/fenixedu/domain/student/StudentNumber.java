package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentNumber extends StudentNumber_Base {

    public StudentNumber(final Student student) {
        super();
        String[] args = {};
        if (student == null) {
            throw new DomainException("error.StudentNumber.invalid.student", args);
        }
        setRootDomainObject(RootDomainObject.getInstance());
        setStudent(student);
        setNumber(student.getNumber());
    }

    public void delete() {
        setRootDomainObject(null);
        setStudent(null);
        super.deleteDomainObject();
    }
}
