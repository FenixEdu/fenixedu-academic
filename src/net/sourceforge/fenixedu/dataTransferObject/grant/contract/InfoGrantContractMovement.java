/*
 * Created on 3/Jul/2004
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractMovement;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantContractMovement;

/**
 * @author Barbosa
 * @author Pica
 */

public class InfoGrantContractMovement extends InfoObject {
    private String location;

    private Date departureDate;

    private Date arrivalDate;

    private InfoGrantContract infoGrantContract;

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
     * @return Returns the infoGrantContract.
     */
    public InfoGrantContract getInfoGrantContract() {
        return this.infoGrantContract;
    }

    /**
     * @param infoGrantContract
     *            The infoGrantContract to set.
     */
    public void setInfoGrantContract(InfoGrantContract infoGrantContract) {
        this.infoGrantContract = infoGrantContract;
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

    /**
     * @param GrantMovement
     */
    public void copyFromDomain(IGrantContractMovement grantMovement) {
        super.copyFromDomain(grantMovement);
        if (grantMovement != null) {
            setLocation(grantMovement.getLocation());
            setArrivalDate(grantMovement.getArrivalDate());
            setDepartureDate(grantMovement.getDepartureDate());
        }
    }

    /**
     * @param GrantMovement
     * @return
     */
    public static InfoGrantContractMovement newInfoFromDomain(IGrantContractMovement grantMovement) {
        InfoGrantContractMovement infoGrantMovement = null;
        if (grantMovement != null) {
            infoGrantMovement = new InfoGrantContractMovement();
            infoGrantMovement.copyFromDomain(grantMovement);
        }
        return infoGrantMovement;
    }

    /**
     * @param grantMovement
     */
    public void copyToDomain(InfoGrantContractMovement infoGrantContractMovement,
            IGrantContractMovement grantContractMovement) {
        super.copyToDomain(infoGrantContractMovement, grantContractMovement);

        grantContractMovement.setLocation(infoGrantContractMovement.getLocation());
        grantContractMovement.setArrivalDate(infoGrantContractMovement.getArrivalDate());
        grantContractMovement.setDepartureDate(infoGrantContractMovement.getDepartureDate());
    }

    /**
     * @param GrantMovement
     * @return
     */
    public static IGrantContractMovement newDomainFromInfo(
            InfoGrantContractMovement infoGrantContractMovement) {
        IGrantContractMovement grantContractMovement = null;
        if (infoGrantContractMovement != null) {
            grantContractMovement = new GrantContractMovement();
            infoGrantContractMovement.copyToDomain(infoGrantContractMovement, grantContractMovement);
        }
        return grantContractMovement;
    }
}