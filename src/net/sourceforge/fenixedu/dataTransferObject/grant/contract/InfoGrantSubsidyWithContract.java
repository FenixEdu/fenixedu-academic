/*
 * Created on Jun 25, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantSubsidyWithContract extends InfoGrantSubsidy {
    public void copyFromDomain(GrantSubsidy grantSubsidy) {
	super.copyFromDomain(grantSubsidy);
	if (grantSubsidy != null) {
	    setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType.newInfoFromDomain(grantSubsidy.getGrantContract()));
	}
    }

    public static InfoGrantSubsidy newInfoFromDomain(GrantSubsidy grantSubsidy) {
	InfoGrantSubsidyWithContract infoGrantSubsidy = null;
	if (grantSubsidy != null) {
	    infoGrantSubsidy = new InfoGrantSubsidyWithContract();
	    infoGrantSubsidy.copyFromDomain(grantSubsidy);
	}
	return infoGrantSubsidy;
    }

}
