package net.sourceforge.fenixedu.domain.support;

public enum SupportRequestType {
    
    SUGESTION,
    DOUBT,
    ERROR,
    EXCEPTION;
    
    public String getName() {
        return name();
    }  
}
