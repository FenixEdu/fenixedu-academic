package net.sourceforge.fenixedu.domain;

public enum AlumniRequestType {

    STUDENT_NUMBER_RECOVERY, IDENTITY_CHECK;

    public String getName() {
	return name();
    }
}
