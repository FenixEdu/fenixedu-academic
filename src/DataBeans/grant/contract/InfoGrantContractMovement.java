/*
 * Created on 3/Jul/2004
 * 
 */
package DataBeans.grant.contract;

import java.util.Date;

import DataBeans.InfoObject;
import Dominio.grant.contract.GrantContractMovement;
import Dominio.grant.contract.IGrantContractMovement;

/**
 * @author Barbosa
 * @author Pica
 */

public class InfoGrantContractMovement extends InfoObject 
{
    private String location;
    private Date departureDate;
    private Date arrivalDate;
    private InfoGrantContract infoGrantContract;
    private Integer keyGrantContract;
    

    /**
     * @return Returns the arrivalDate.
     */
    public Date getArrivalDate() {
        return this.arrivalDate;
    }
    /**
     * @param arrivalDate The arrivalDate to set.
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
     * @param departureDate The departureDate to set.
     */
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }
    /**
     * @return Returns the infoGrantContract.
     */
    public InfoGrantContract getInfoGrantContract() {
        return this.infoGrantContract;
    }
    /**
     * @param infoGrantContract The infoGrantContract to set.
     */
    public void setInfoGrantContract(InfoGrantContract infoGrantContract) {
        this.infoGrantContract = infoGrantContract;
    }
    /**
     * @return Returns the keyGrantContract.
     */
    public Integer getKeyGrantContract() {
        return this.keyGrantContract;
    }
    /**
     * @param keyGrantContract The keyGrantContract to set.
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
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /**
     * @param GrantMovement
     */
    public void copyFromDomain(IGrantContractMovement grantMovement)
    {
    	super.copyFromDomain(grantMovement);
    	if (grantMovement != null)
    	{
    		setLocation(grantMovement.getLocation());
    		setArrivalDate(grantMovement.getArrivalDate());
    		setDepartureDate(grantMovement.getDepartureDate());
    	}
    }
    
    /**
     * @param GrantMovement
     * @return
     */
    public static InfoGrantContractMovement newInfoFromDomain(IGrantContractMovement grantMovement)
    {
    	InfoGrantContractMovement infoGrantMovement = null;
    	if (grantMovement != null)
    	{
    		infoGrantMovement = new InfoGrantContractMovement();
    		infoGrantMovement.copyFromDomain(grantMovement);
    	}
    	return infoGrantMovement;
    }
    
    /**
     * @param grantMovement
     */
    public IGrantContractMovement copyToDomain()
    {
        IGrantContractMovement grantContractMovement = new GrantContractMovement();
    	super.copyToDomain(grantContractMovement);
    	if (grantContractMovement != null)
    	{
    		grantContractMovement.setLocation(getLocation());
    		grantContractMovement.setArrivalDate(getArrivalDate());
    		grantContractMovement.setDepartureDate(getDepartureDate());
    	}
    	return grantContractMovement;
    }
    
    /**
     * @param GrantMovement
     * @return
     */
    public IGrantContractMovement newDomainFromInfo()
    {
    	return copyToDomain();
    }
}
