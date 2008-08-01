package net.sourceforge.fenixedu.domain.support;

public enum SupportRequestPriority {

    EXCEPTION,
    NOT_IMPORTANT,
    SERIOUS,
    IMPEDIMENT;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return SupportRequestPriority.class.getSimpleName() + "." + name();
    }

}
