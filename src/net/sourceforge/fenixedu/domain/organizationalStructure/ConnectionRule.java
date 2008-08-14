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
	removeAccountabilityType();
	removeAllowedChildPartyType();
	removeAllowedParentPartyType();
	removeRootDomainObject();
	deleteDomainObject();
    }
}
