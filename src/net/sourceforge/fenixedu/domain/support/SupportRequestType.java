package net.sourceforge.fenixedu.domain.support;

public enum SupportRequestType {
    
    SUGESTION,
    REQUEST,
    DOUBT,
    ERROR,
    EXCEPTION;
    
    public String getName() {
        return name();
    }
    
    public String getQualifiedName() {
	return SupportRequestType.class.getSimpleName() + "." + name();
    }
}
