/*
 * Created on 5/Maio/2004
 * 
 */
package Dominio.grant.contract;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.ITeacher;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public interface IGrantContractRegime extends IDomainObject {

    public Integer getState();

    public Date getDateBeginContract();

    public Date getDateEndContract();

    public Date getDateDispatchCC();

    public Date getDateDispatchCD();

    public Date getDateSendDispatchCC();

    public Date getDateSendDispatchCD();

    public IGrantContract getGrantContract();

    public ITeacher getTeacher();

    public boolean getContractRegimeActive();
    
    public Integer getCostCenterKey();
    
    public IGrantCostCenter getGrantCostCenter();

    public void setState(Integer state);

    public void setDateBeginContract(Date dateBeginContract);

    public void setDateEndContract(Date dateEndContract);

    public void setDateDispatchCC(Date dateDispatchCC);

    public void setDateDispatchCD(Date dateDispatchCD);

    public void setDateSendDispatchCC(Date dateSendDispatchCC);

    public void setDateSendDispatchCD(Date dateSendDispatchCD);

    public void setGrantContract(IGrantContract grantContract);

    public void setTeacher(ITeacher teacher);
    
    public void setCostCenterKey(Integer costCenterkey);
    
    public void setGrantCostCenter(IGrantCostCenter grantCostCenter);
}