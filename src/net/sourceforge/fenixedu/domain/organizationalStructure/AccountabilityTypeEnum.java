/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

public enum AccountabilityTypeEnum {

    MANAGEMENT_FUNCTION,
    
    ORGANIZATIONAL_STRUCTURE,
    ACADEMIC_STRUCTURE,     
    ADMINISTRATIVE_STRUCTURE,
    
    GEOGRAPHIC,
    
    //Contracts
    WORKING_CONTRACT,
    MAILING_CONTRACT,         
    INVITATION; 
         
    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return AccountabilityTypeEnum.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return AccountabilityTypeEnum.class.getName() + "." + name();
    }
}