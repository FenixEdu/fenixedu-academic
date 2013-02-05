package net.sourceforge.fenixedu.domain.teacher;

public enum CategoryType {

    TEACHER, EMPLOYEE, RESEARCHER, GRANT_OWNER;

    public String getName() {
        return name();
    }
}
