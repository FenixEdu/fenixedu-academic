/*
 * Created on 3/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractMovement;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractMovementWithContract extends InfoGrantContractMovement {

    public void copyFromDomain(IGrantContractMovement grantMovement) {
        super.copyFromDomain(grantMovement);
        if (grantMovement != null) {
            setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain(grantMovement.getGrantContract()));
        }
    }

    public static InfoGrantContractMovement newInfoFromDomain(IGrantContractMovement grantMovement) {
        InfoGrantContractMovementWithContract infoGrantContractMovementWithContract = null;
        if (grantMovement != null) {
            infoGrantContractMovementWithContract = new InfoGrantContractMovementWithContract();
            infoGrantContractMovementWithContract.copyFromDomain(grantMovement);
        }
        return infoGrantContractMovementWithContract;
    }

}
