package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class AccountabilityType extends AccountabilityType_Base {
    
    protected AccountabilityType() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());        
        setOjbConcreteClass(getClass().getName());
    }
    
    public AccountabilityType(AccountabilityTypeEnum accountabilityTypeEnum) {
        this();
        setType(accountabilityTypeEnum);
    }

    public static AccountabilityType readAccountabilityTypeByType(AccountabilityTypeEnum typeEnum) {
        List<AccountabilityType> allAccountabilityTypes = RootDomainObject.getInstance().getAccountabilityTypes();
        for (AccountabilityType accountabilityType : allAccountabilityTypes) {
            if (accountabilityType.getType().equals(typeEnum)) {
                return accountabilityType;
            }
        }
        return null;
    }   
}
