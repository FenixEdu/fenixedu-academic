/*
 * Created on 27/Out/2003
 * 
 */
package Dominio.grant.owner;

import java.util.Date;
import java.util.List;

import Dominio.DomainObject;
import Dominio.IPerson;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class GrantOwner extends DomainObject implements IGrantOwner {

    private Integer number;

    private Date dateSendCGD;

    private Integer cardCopyNumber;

    private IPerson person;

    private Integer keyPerson;

    private List grantContracts; //Used for querys (See GrantOwnerOJB or

    // GrantContractOJB class)

    /**
     * Constructor
     */
    public GrantOwner() {
    }

    /**
     * Constructor
     */
    public GrantOwner(Integer grantOwnerId) {
        setIdInternal(grantOwnerId);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IGrantOwner) {
            IGrantOwner grantOwner = (IGrantOwner) obj;
            result = (((this.number.equals(grantOwner.getNumber())) && (this.person.equals(grantOwner
                    .getPerson()))));
        }
        return result;
    }

    /**
     * @return Date
     */
    public Date getDateSendCGD() {
        return dateSendCGD;
    }

    /**
     * @return Integer
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @return Integer
     */
    public Integer getKeyPerson() {
        return keyPerson;
    }

    /**
     * @return Integer
     */
    public Integer getCardCopyNumber() {
        return cardCopyNumber;
    }

    /**
     * @return Integer
     */
    public IPerson getPerson() {
        return person;
    }

    /**
     * Sets the dateSendCGD.
     * 
     * @param dateSendCGD
     *            The dateSendCGD to set
     */
    public void setDateSendCGD(Date dateSendCGD) {
        this.dateSendCGD = dateSendCGD;
    }

    /**
     * Sets the number of the grantOwner.
     * 
     * @param number
     *            The number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * Sets the Person.
     * 
     * @param Person
     *            The Person to set
     */
    public void setPerson(IPerson person) {
        this.person = person;
    }

    /**
     * Sets the cardCopyNumber.
     * 
     * @param cardCopyNumber
     *            The cardCopyNumber to set
     */
    public void setCardCopyNumber(Integer cardCopyNumber) {
        this.cardCopyNumber = cardCopyNumber;
    }

    /**
     * Sets the keyPerson.
     * 
     * @param personKey
     *            The personKey to set
     */
    public void setKeyPerson(Integer personKey) {
        this.keyPerson = personKey;
    }

    /**
     * @return Returns the grantContracts.
     */
    public List getGrantContracts() {
        return grantContracts;
    }

    /**
     * @param grantContracts
     *            The grantContracts to set.
     */
    public void setGrantContracts(List grantContracts) {
        this.grantContracts = grantContracts;
    }
}