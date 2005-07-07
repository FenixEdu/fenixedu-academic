/*
 * Created on Jun 25, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantSubsidyWithContract extends InfoGrantSubsidy {
    public void copyFromDomain(IGrantSubsidy grantSubsidy) {
        super.copyFromDomain(grantSubsidy);
        if (grantSubsidy != null) {
            setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain(grantSubsidy.getGrantContract()));
        }
    }

    public static InfoGrantSubsidy newInfoFromDomain(IGrantSubsidy grantSubsidy) {
        InfoGrantSubsidyWithContract infoGrantSubsidy = null;
        if (grantSubsidy != null) {
            infoGrantSubsidy = new InfoGrantSubsidyWithContract();
            infoGrantSubsidy.copyFromDomain(grantSubsidy);
        }
        return infoGrantSubsidy;
    }

    public void copyToDomain(InfoGrantSubsidy infoGrantSubsidy, IGrantSubsidy grantSubsidy) throws ExcepcaoPersistencia {
        super.copyToDomain(infoGrantSubsidy, grantSubsidy);
        grantSubsidy.setGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                .newDomainFromInfo(infoGrantSubsidy.getInfoGrantContract()));
    }

    public static IGrantSubsidy newDomainFromInfo(InfoGrantSubsidy infoGrantSubsidy) throws ExcepcaoPersistencia {
        IGrantSubsidy grantSubsidy = null;
        InfoGrantSubsidyWithContract infoGrantSubsidyWithContract = null;
        if (infoGrantSubsidy != null) {
            grantSubsidy = new GrantSubsidy();
            infoGrantSubsidyWithContract = new InfoGrantSubsidyWithContract();
            infoGrantSubsidyWithContract.copyToDomain(infoGrantSubsidy, grantSubsidy);
        }
        return grantSubsidy;
    }

}