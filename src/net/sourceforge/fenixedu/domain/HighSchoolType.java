package net.sourceforge.fenixedu.domain;

public enum HighSchoolType {

    PUBLIC,

    PRIVATE,

    BOTH;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return HighSchoolType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return HighSchoolType.class.getName() + "." + name();
    }

}
