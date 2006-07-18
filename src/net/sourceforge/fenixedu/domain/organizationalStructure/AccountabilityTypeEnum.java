/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

public enum AccountabilityTypeEnum {

    MANAGEMENT_FUNCTION,
    ACADEMIC_STRUCTURE,
    ORGANIZATIONAL_STRUCTURE,
    ACADEMIC_MANAGEMENT;
        
    public String getName() {
        return name();
    }      
}
