package net.sourceforge.fenixedu.domain;

public enum AlumniRequestType {

    STUDENT_NUMBER_RECOVERY, IDENTITY_CHECK, PASSWORD_REQUEST;

    public String getName() {
        return name();
    }
}
