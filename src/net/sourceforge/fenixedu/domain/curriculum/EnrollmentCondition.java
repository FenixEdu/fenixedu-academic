package net.sourceforge.fenixedu.domain.curriculum;


/**
 * @author David Santos in Jun 15, 2004
 */

public enum EnrollmentCondition {
    
    FINAL,
    
    TEMPORARY,
    
    IMPOSSIBLE,
    
    VALIDATED,
    
    INVISIBLE;
    
    public String getName() {
    	return name();
    }
    
}