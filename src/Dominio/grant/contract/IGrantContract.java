/*
 * Created on 18/Nov/2003
 * 
 */
package Dominio.grant.contract;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.grant.owner.IGrantOwner;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public interface IGrantContract extends IDomainObject
{
	public Integer getContractNumber();
	public Date getDateBeginContract();
	public Date getDateEndContract();
	public String getEndContractMotive();
	public IGrantOwner getGrantOwner();
	public IGrantType getGrantType();

	public void setContractNumber(Integer contractNumber);
	public void setDateBeginContract(Date dateBeginContract);
	public void setDateEndContract(Date dateEndContract);
	public void setEndContractMotive(String endContractMotive);
	public void setGrantOwner(IGrantOwner grantOwner);
	public void setGrantType(IGrantType grantType);
}