/*
 * Created on 3/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
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

    /**
     * @param grantMovement
     */
    public void copyToDomain(InfoGrantContractMovement infoGrantContractMovement,
            IGrantContractMovement grantContractMovement) {
        super.copyToDomain(infoGrantContractMovement, grantContractMovement);

        grantContractMovement.setGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                .newDomainFromInfo(infoGrantContractMovement.getInfoGrantContract()));
    }

    /**
     * @param GrantMovement
     * @return
     */
    public static IGrantContractMovement newDomainFromInfo(
            InfoGrantContractMovement infoGrantContractMovement) {
        IGrantContractMovement grantContractMovement = null;
        InfoGrantContractMovementWithContract infoGrantContractMovementWithContract = null;
        if (infoGrantContractMovement != null) {
            grantContractMovement = new GrantContractMovement();
            infoGrantContractMovementWithContract = new InfoGrantContractMovementWithContract();
            infoGrantContractMovementWithContract.copyToDomain(infoGrantContractMovement,
                    grantContractMovement);
        }
        return grantContractMovement;
    }
}