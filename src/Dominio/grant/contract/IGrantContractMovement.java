/*
 * Created on 2/Jul/2004
 * 
 */
package Dominio.grant.contract;

import java.util.Date;

import Dominio.IDomainObject;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public interface IGrantContractMovement extends IDomainObject 
{
    public Date getArrivalDate();
    public Date getDepartureDate();
    public IGrantContract getGrantContract();
    public Integer getKeyGrantContract();
    public String getLocation();
    
    public void setArrivalDate(Date arrivalDate);
    public void setDepartureDate(Date departureDate);
    public void setGrantContract(IGrantContract grantContract);
    public void setKeyGrantContract(Integer keyGrantContract);
    public void setLocation(String location);
}
