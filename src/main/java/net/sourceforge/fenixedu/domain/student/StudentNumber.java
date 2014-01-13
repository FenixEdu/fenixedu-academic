package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

public class StudentNumber extends StudentNumber_Base {

    public StudentNumber(final Student student) {
        super();
        String[] args = {};
        if (student == null) {
            throw new DomainException("error.StudentNumber.invalid.student", args);
        }
        setRootDomainObject(Bennu.getInstance());
        setStudent(student);
        setNumber(student.getNumber());
    }

    public void delete() {
        setRootDomainObject(null);
        setStudent(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

}
