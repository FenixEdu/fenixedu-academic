package net.sourceforge.fenixedu.domain.organizationalStructure;

public class ConnectionRule extends ConnectionRule_Base {
    
    public ConnectionRule(){
        super();
    }
    
    public ConnectionRule(PartyType allowedParentPartyType, PartyType allowedChildPartyType, AccountabilityType accountabilityType) {        
        super();
        setAllowedParentPartyType(allowedParentPartyType);
        setAllowedChildPartyType(allowedChildPartyType);
        setAccountabilityType(accountabilityType);
    }
    
    public void delete(){
        removeAccountabilityType();
        removeAllowedChildPartyType();
        removeAllowedParentPartyType();
        deleteDomainObject();
    }
}
