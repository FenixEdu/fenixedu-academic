package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ConnectionRule extends ConnectionRule_Base {

    public ConnectionRule() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public ConnectionRule(PartyType allowedParentPartyType, PartyType allowedChildPartyType, AccountabilityType accountabilityType) {
        this();
        setAllowedParentPartyType(allowedParentPartyType);
        setAllowedChildPartyType(allowedChildPartyType);
        setAccountabilityType(accountabilityType);
    }

    public void delete() {
        setAccountabilityType(null);
        setAllowedChildPartyType(null);
        setAllowedParentPartyType(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }
    @Deprecated
    public boolean hasAccountabilityType() {
        return getAccountabilityType() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasAllowedParentPartyType() {
        return getAllowedParentPartyType() != null;
    }

    @Deprecated
    public boolean hasAllowedChildPartyType() {
        return getAllowedChildPartyType() != null;
    }

}
