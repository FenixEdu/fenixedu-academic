/*
 * Created on 2/Jul/2004
 * 
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public interface IGrantContractMovement extends IDomainObject {
    public Date getArrivalDate();

    public Date getDepartureDate();

    public IGrantContract getGrantContract();

    public String getLocation();

    public void setArrivalDate(Date arrivalDate);

    public void setDepartureDate(Date departureDate);

    public void setGrantContract(IGrantContract grantContract);

    public void setLocation(String location);
}