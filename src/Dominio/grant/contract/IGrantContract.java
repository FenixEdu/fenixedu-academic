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
    public Date getDateAcceptTerm();
    public Date getDateDispatchCC();
    public Date getDateDispatchCD();
    public Date getDateSendDispatchCC();
    public Date getDateSendDispatchCD();
    
	public void setContractNumber(Integer contractNumber);
	public void setDateBeginContract(Date dateBeginContract);
	public void setDateEndContract(Date dateEndContract);
	public void setEndContractMotive(String endContractMotive);
	public void setGrantOwner(IGrantOwner grantOwner);
	public void setGrantType(IGrantType grantType);
    public void setDateAcceptTerm(Date dateAcceptTerm);
    public void setDateDispatchCC(Date dateDispatchCC);
    public void setDateDispatchCD(Date dateDispatchCD);
    public void setDateSendDispatchCC(Date dateSendDispatchCC);
    public void setDateSendDispatchCD(Date dateSendDispatchCD);
}