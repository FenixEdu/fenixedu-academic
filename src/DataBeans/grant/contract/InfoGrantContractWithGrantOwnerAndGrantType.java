/*
 * Created on Jun 18, 2004
 *
 */
package DataBeans.grant.contract;

import DataBeans.grant.owner.InfoGrantOwnerWithPerson;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.IGrantContract;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractWithGrantOwnerAndGrantType extends InfoGrantContract {

    public void copyFromDomain(IGrantContract grantContract) {
        super.copyFromDomain(grantContract);
        if (grantContract != null) {

            setGrantOwnerInfo(InfoGrantOwnerWithPerson.newInfoFromDomain(grantContract.getGrantOwner()));
            setGrantTypeInfo(InfoGrantType.newInfoFromDomain(grantContract.getGrantType()));
        }
    }

    public static InfoGrantContract newInfoFromDomain(IGrantContract grantContract) {
        InfoGrantContractWithGrantOwnerAndGrantType infoGrantContract = null;
        if (grantContract != null) {
            infoGrantContract = new InfoGrantContractWithGrantOwnerAndGrantType();
            infoGrantContract.copyFromDomain(grantContract);
        }
        return infoGrantContract;
    }

    public void copyToDomain(InfoGrantContract infoGrantContract, IGrantContract grantContract) {
        super.copyToDomain(infoGrantContract, grantContract);
        grantContract.setGrantOwner(InfoGrantOwnerWithPerson.newDomainFromInfo(infoGrantContract
                .getGrantOwnerInfo()));
        grantContract
                .setGrantType(InfoGrantType.newDomainFromInfo(infoGrantContract.getGrantTypeInfo()));
    }

    public static IGrantContract newDomainFromInfo(InfoGrantContract infoGrantContract) {
        IGrantContract grantContract = null;
        InfoGrantContractWithGrantOwnerAndGrantType infoGrantContractWithGrantOwnerAndGrantType = null;
        if (infoGrantContract != null) {
            grantContract = new GrantContract();
            infoGrantContractWithGrantOwnerAndGrantType = new InfoGrantContractWithGrantOwnerAndGrantType();
            infoGrantContractWithGrantOwnerAndGrantType.copyToDomain(infoGrantContract, grantContract);
        }
        return grantContract;
    }

}