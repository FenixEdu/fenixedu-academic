package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantType extends GrantType_Base {

    public GrantType() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static GrantType readBySigla(String sigla) {
	for (GrantType grantType : RootDomainObject.getInstance().getGrantTypes()) {
	    if (grantType.getSigla().equals(sigla)) {
		return grantType;
	    }
	}
	return null;
    }

    public int countGrantContractsByActiveAndDate(final Boolean activeContracts, final Date beginContractDate,
	    final Date endContractDate) {

	int count = 0;
	for (final GrantContract grantContract : this.getAssociatedGrantContractsSet()) {
	    if (activeContracts != null && activeContracts) {
		if (!grantContract.hasActiveRegimes() || grantContract.getEndContractMotive().length() != 0) {
		    continue;
		}
	    } else if (activeContracts != null && !activeContracts) {
		if (grantContract.hasActiveRegimes() || grantContract.getEndContractMotive().length() == 0) {
		    continue;
		}
	    }
	    if (!grantContract.hasRegimesInPeriod(beginContractDate, endContractDate)) {
		continue;
	    }
	    count++;
	}
	return count;
    }

}
