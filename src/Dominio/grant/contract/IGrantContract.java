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
public interface IGrantContract extends IDomainObject {

    public Integer getContractNumber();

    public String getEndContractMotive();

    public IGrantOwner getGrantOwner();

    public IGrantType getGrantType();

    public Date getDateAcceptTerm();
    
    public Integer getCostCenterKey();
    
    public IGrantCostCenter getGrantCostCenter();

    public void setContractNumber(Integer contractNumber);

    public void setEndContractMotive(String endContractMotive);

    public void setGrantOwner(IGrantOwner grantOwner);

    public void setGrantType(IGrantType grantType);

    public void setDateAcceptTerm(Date dateAcceptTerm);
    
    public void setCostCenterKey(Integer costCenterkey);
    
    public void setGrantCostCenter(IGrantCostCenter grantCostCenter);
}