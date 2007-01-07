/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

public enum AccountabilityTypeEnum {

    MANAGEMENT_FUNCTION, ACADEMIC_STRUCTURE, ORGANIZATIONAL_STRUCTURE, EMPLOYEE_CONTRACT, INVITATION, ADMINISTRATIVE_STRUCTURE, GEOGRAPHIC;
    
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
