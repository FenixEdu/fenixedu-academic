/*
 * Created on Jun 18, 2004
 *
 */
package DataBeans.grant.contract;

import DataBeans.grant.owner.InfoGrantOwnerWithPerson;
import Dominio.grant.contract.IGrantContract;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractWithGrantOwnerAndGrantType extends
        InfoGrantContract {

    public void copyFromDomain(IGrantContract grantContract) {
        super.copyFromDomain(grantContract);
        if (grantContract != null) {

            setGrantOwnerInfo(InfoGrantOwnerWithPerson
                    .newInfoFromDomain(grantContract.getGrantOwner()));
            setGrantTypeInfo(InfoGrantType.newInfoFromDomain(grantContract
                    .getGrantType()));
        }
    }

    public static InfoGrantContract newInfoFromDomain(
            IGrantContract grantContract) {
        InfoGrantContractWithGrantOwnerAndGrantType infoGrantContract = null;
        if (grantContract != null) {
            infoGrantContract = new InfoGrantContractWithGrantOwnerAndGrantType();
            infoGrantContract.copyFromDomain(grantContract);
        }
        return infoGrantContract;
    }

    public IGrantContract copyToDomain()
    {
        IGrantContract grantContract = super.copyToDomain();
        if (grantContract != null)
    	{
            InfoGrantOwnerWithPerson infoGrantOwnerWithPerson = (InfoGrantOwnerWithPerson) super.getGrantOwnerInfo();
    		grantContract.setGrantOwner(infoGrantOwnerWithPerson.newDomainFromInfo());
    		grantContract.setGrantType(super.getGrantTypeInfo().newDomainFromInfo());
    	}
        return grantContract;
    }
    
    public IGrantContract newDomainFromInfo()
    {
        return this.copyToDomain();
    }
    
}
