/*
 * Created on 3/Jul/2004
 *
 */
package DataBeans.grant.contract;

import Dominio.grant.contract.IGrantContractMovement;


/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractMovementWithContract extends InfoGrantContractMovement
{
	public void copyFromDomain(IGrantContractMovement grantMovement)
	{
		super.copyFromDomain(grantMovement);
		if(grantMovement != null){
		    setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType.newInfoFromDomain(grantMovement.getGrantContract()));
		}
	}
	
	public static InfoGrantContractMovement newInfoFromDomain(IGrantContractMovement grantMovement)
	{
		InfoGrantContractMovementWithContract infoGrantContractMovementWithContract = null;
		if(grantMovement != null){
		    infoGrantContractMovementWithContract = new InfoGrantContractMovementWithContract();
		    infoGrantContractMovementWithContract.copyFromDomain(grantMovement);
		}
		return infoGrantContractMovementWithContract;
	}
	
	/**
     * @param grantMovement
     */
    public IGrantContractMovement copyToDomain()
    {
    	IGrantContractMovement grantContractMovement = super.copyToDomain();
    	if (grantContractMovement != null)
    	{
    	    InfoGrantContractWithGrantOwnerAndGrantType infoGrantContractWithGrantOwnerAndGrantType = (InfoGrantContractWithGrantOwnerAndGrantType) getInfoGrantContract();
    		grantContractMovement.setGrantContract(infoGrantContractWithGrantOwnerAndGrantType.newDomainFromInfo());
    	}
    	return grantContractMovement;
    }
    
    /**
     * @param GrantMovement
     * @return
     */
    public IGrantContractMovement newDomainFromInfo()
    {
    	return this.copyToDomain();
    }
}
