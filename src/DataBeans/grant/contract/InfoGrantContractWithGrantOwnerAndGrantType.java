/*
 * Created on Jun 18, 2004
 *
 */
package DataBeans.grant.contract;

import DataBeans.grant.owner.InfoGrantOwner;
import Dominio.grant.contract.IGrantContract;


/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractWithGrantOwnerAndGrantType extends InfoGrantContract
{
	public void copyFromDomain(IGrantContract grantContract)
	{
		super.copyFromDomain(grantContract);
		if(grantContract != null){
			
			setGrantOwnerInfo(InfoGrantOwner.newInfoFromDomain(grantContract.getGrantOwner())); //Se calhar temos que usar o InfoGrantOwnerWithPerson
			//setGrantOrientationTeacherInfo(InfoGrantOrientationTeacher.newInfoFromDomain
			setGrantTypeInfo(InfoGrantType.newInfoFromDomain(grantContract.getGrantType()));
		}
	}
	
	public static InfoGrantContract newInfoFromDomain(IGrantContract grantContract)
	{
		InfoGrantContractWithGrantOwnerAndGrantType infoGrantContract = null;
		if(grantContract != null){
			infoGrantContract = new InfoGrantContractWithGrantOwnerAndGrantType();
			infoGrantContract.copyFromDomain(grantContract);
		}
		return infoGrantContract;
	}
	
}
