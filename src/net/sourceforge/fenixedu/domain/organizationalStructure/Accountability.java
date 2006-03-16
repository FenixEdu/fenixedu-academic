package net.sourceforge.fenixedu.domain.organizationalStructure;

public class Accountability extends Accountability_Base {
    
    public Accountability(Party parentParty, Party childParty, AccountabilityType accountabilityType) {     
        super();
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
