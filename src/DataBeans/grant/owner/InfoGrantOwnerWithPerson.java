/*
 * Created on Jun 18, 2004
 *
 */
package DataBeans.grant.owner;

import DataBeans.InfoPersonWithInfoCountry;
import Dominio.grant.owner.IGrantOwner;


/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantOwnerWithPerson extends InfoGrantOwner
{
	public void copyFromDomain(IGrantOwner grantOwner)
	{
		super.copyFromDomain(grantOwner);
		if(grantOwner != null){
			setPersonInfo(InfoPersonWithInfoCountry.newInfoFromDomain(grantOwner.getPerson()));
		}
	}
	
	public static InfoGrantOwner newInfoFromDomain(IGrantOwner grantOwner)
	{
		InfoGrantOwnerWithPerson infoGrantOwner = null;
		if(grantOwner != null){
			infoGrantOwner = new InfoGrantOwnerWithPerson();
			infoGrantOwner.copyFromDomain(grantOwner);
		}
		return infoGrantOwner;
	}
	
	public IGrantOwner copyToDomain()
    {
        IGrantOwner grantOwner = super.copyToDomain();
        if (grantOwner != null)
    	{
            InfoPersonWithInfoCountry infoPersonWithInfoCountry = (InfoPersonWithInfoCountry) super.getPersonInfo();
    		grantOwner.setPerson(infoPersonWithInfoCountry.newDomainFromInfo());
    	}
        return grantOwner;
    }
    
    public IGrantOwner newDomainFromInfo()
    {
        return this.copyToDomain();
    }
	
}
