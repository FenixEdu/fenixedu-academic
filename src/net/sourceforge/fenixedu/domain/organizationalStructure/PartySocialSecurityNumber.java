package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PartySocialSecurityNumber extends PartySocialSecurityNumber_Base {

    private PartySocialSecurityNumber() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public PartySocialSecurityNumber(final Party party, final String socialSecurityNumber) {
	this();
	checkParameters(party, socialSecurityNumber);
	super.setParty(party);
	super.setSocialSecurityNumber(socialSecurityNumber);
    }

    private void checkParameters(final Party party, final String socialSecurityNumber) {
	if (party == null) {
	    throw new DomainException("error.PartySocialSecurityNumber.invalid.party");
	}
	if (socialSecurityNumber == null || socialSecurityNumber.length() == 0) {
	    throw new DomainException("error.PartySocialSecurityNumber.invalid.socialSecurityNumber");
	}

	for (final PartySocialSecurityNumber securityNumber : RootDomainObject.getInstance().getPartySocialSecurityNumbers()) {
	    if (securityNumber != this && securityNumber.hasSocialSecurityNumber(socialSecurityNumber)) {
		throw new DomainException("error.PartySocialSecurityNumber.number.already.exists");
	    }
	}
    }

    public boolean hasSocialSecurityNumber(String socialSecurityNumber) {
	return getSocialSecurityNumber().equals(socialSecurityNumber);
    }

    public void delete() {
	removeParty();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public static Party readPartyBySocialSecurityNumber(final String socialSecurityNumber) {
	if (socialSecurityNumber == null || socialSecurityNumber.length() == 0) {
	    return null;
	}
	for (final PartySocialSecurityNumber securityNumber : RootDomainObject.getInstance().getPartySocialSecurityNumbers()) {
	    if (securityNumber.hasSocialSecurityNumber(socialSecurityNumber)) {
		return securityNumber.getParty();
	    }
	}
	return null;
    }
}
