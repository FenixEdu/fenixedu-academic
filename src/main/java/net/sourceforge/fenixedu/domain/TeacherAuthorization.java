package net.sourceforge.fenixedu.domain;

public class TeacherAuthorization extends TeacherAuthorization_Base {

    public TeacherAuthorization() {
        super();
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

    @Deprecated
    public boolean hasProfessionalCategory() {
        return getProfessionalCategory() != null;
    }

}
