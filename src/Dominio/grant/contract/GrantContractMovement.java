/*
 * Created on 3/Jul/2004
 * 
 */
package Dominio.grant.contract;

import java.util.Date;

import Dominio.DomainObject;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantContractMovement extends DomainObject implements IGrantContractMovement {
    private String location;

    private Date departureDate;

    private Date arrivalDate;

    private IGrantContract grantContract;

    private Integer keyGrantContract;

    /**
     * @return Returns the arrivalDate.
     */
    public Date getArrivalDate() {
        return this.arrivalDate;
    }

    /**
     * @param arrivalDate
     *            The arrivalDate to set.
     */
    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    /**
     * @return Returns the departureDate.
     */
    public Date getDepartureDate() {
        return this.departureDate;
    }

    /**
     * @param departureDate
     *            The departureDate to set.
     */
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * @return Returns the grantContract.
     */
    public IGrantContract getGrantContract() {
        return this.grantContract;
    }

    /**
     * @param grantContract
     *            The grantContract to set.
     */
    public void setGrantContract(IGrantContract grantContract) {
        this.grantContract = grantContract;
    }

    /**
     * @return Returns the keyGrantContract.
     */
    public Integer getKeyGrantContract() {
        return this.keyGrantContract;
    }

    /**
     * @param keyGrantContract
     *            The keyGrantContract to set.
     */
    public void setKeyGrantContract(Integer keyGrantContract) {
        this.keyGrantContract = keyGrantContract;
    }

    /**
     * @return Returns the location.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @param location
     *            The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

}