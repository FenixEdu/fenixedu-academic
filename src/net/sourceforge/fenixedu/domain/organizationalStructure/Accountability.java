package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Accountability extends Accountability_Base {
    
    public Accountability(Party parentParty, Party childParty, AccountabilityType accountabilityType) {     
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParentParty(parentParty);
        setChildParty(childParty);
        setAccountabilityType(accountabilityType);
    }
        
    public void delete(){
        removeAccountabilityType();
        removeChildParty();
        removeParentParty();
        deleteDomainObject();
    }
}
