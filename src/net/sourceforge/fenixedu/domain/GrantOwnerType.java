package net.sourceforge.fenixedu.domain;

public enum GrantOwnerType {

    STUDENT_WITHOUT_SCHOLARSHIP,

    HIGHER_EDUCATION_SAS_GRANT_OWNER_CANDIDATE,

    FCT_GRANT_OWNER,

    ORIGIN_COUNTRY_GRANT_OWNER,

    OTHER_INSTITUTION_GRANT_OWNER;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return GrantOwnerType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return GrantOwnerType.class.getName() + "." + name();
    }

}
