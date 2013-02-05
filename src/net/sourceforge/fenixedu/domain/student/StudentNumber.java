package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class StudentNumber extends StudentNumber_Base {

    public StudentNumber(final Student student) {
        super();
        check(student, "error.StudentNumber.invalid.student");
        setRootDomainObject(RootDomainObject.getInstance());
        setStudent(student);
        setNumber(student.getNumber());
    }

    public void delete() {
        removeRootDomainObject();
        removeStudent();
        super.deleteDomainObject();
    }
}
