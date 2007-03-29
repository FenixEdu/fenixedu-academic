package net.sourceforge.fenixedu.domain.research.result.publication;


public enum ScopeType {

    NATIONAL, INTERNATIONAL;

    public static ScopeType getDefaultType() {
	return NATIONAL;
    }

    public String getName() {
	return name();

    }
}
