package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class MonthlyBalance extends MonthlyBalance_Base {
    
    public  MonthlyBalance() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
