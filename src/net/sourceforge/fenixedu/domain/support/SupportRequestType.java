package net.sourceforge.fenixedu.domain.support;

public enum SupportRequestType {

    SUGESTION, DEMAND, DOUBT, ERROR, EXCEPTION;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return SupportRequestType.class.getSimpleName() + "." + name();
    }
}
