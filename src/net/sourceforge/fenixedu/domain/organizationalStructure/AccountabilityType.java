package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class AccountabilityType extends AccountabilityType_Base {
    
    public AccountabilityType(AccountabilityTypeEnum accountabilityTypeEnum) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setType(accountabilityTypeEnum);
    }   
}
