package net.sourceforge.fenixedu.domain;

public enum JobApplicationType {

    ANNOUNCEMENT, 
    PUBLIC_ANNOUNCEMENT, 
    SPONTANEOUS_PROPOSAL, 
    EMPLOYMENT_AGENCY, 
    DEPARTMENTS, 
    UNIVA, 
    AEIST,
    IAESTE,
    IEFP,
    PERSONAL_CONTACT,
    ENTERPRENEURSHIP,
    HEAD_HUNTERS,
    OTHERS;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return JobApplicationType.class.getSimpleName() + "." + name();
    }

}
