package net.sourceforge.fenixedu.domain.support;

public enum SupportRequestPriority {
    
    NOT_IMPORTANT,
    SERIOUS,
    IMPEDIMENT;
    
    public String getName() {
        return name();
    }  
}
