/*
 * Created on Jun 26, 2004
 */
package DataBeans.grant.contract;

import Dominio.grant.contract.IGrantInsurance;


/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantInsuranceWithContract extends InfoGrantInsurance {

    public void copyFromDomain(IGrantInsurance grantInsurance)
	{
		super.copyFromDomain(grantInsurance);
		if(grantInsurance != null){
		    setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType.newInfoFromDomain(grantInsurance.getGrantContract()));
		}
	}
	
	public static InfoGrantInsurance newInfoFromDomain(IGrantInsurance grantInsurance)
	{
		InfoGrantInsuranceWithContract infoGrantInsuranceWithContract = null;
		if(grantInsurance != null){
			infoGrantInsuranceWithContract = new InfoGrantInsuranceWithContract();
			infoGrantInsuranceWithContract.copyFromDomain(grantInsurance);
		}
		return infoGrantInsuranceWithContract;
	}

}
