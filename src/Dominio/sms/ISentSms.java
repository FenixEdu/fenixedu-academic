/*
 * Created on 7/Jun/2004
 *
 */
package Dominio.sms;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.IPessoa;
import Util.SmsDeliveryType;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface ISentSms extends IDomainObject {
    /**
     * @return Returns the deliveryDate.
     */
    public abstract Date getDeliveryDate();

    /**
     * @param deliveryDate
     *            The deliveryDate to set.
     */
    public abstract void setDeliveryDate(Date deliveryDate);

    /**
     * @return Returns the deliveryType.
     */
    public abstract SmsDeliveryType getDeliveryType();

    /**
     * @param deliveryType
     *            The deliveryType to set.
     */
    public abstract void setDeliveryType(SmsDeliveryType deliveryType);

    /**
     * @return Returns the destinationNumber.
     */
    public abstract Integer getDestinationNumber();

    /**
     * @param destinationNumber
     *            The destinationNumber to set.
     */
    public abstract void setDestinationNumber(Integer destinationNumber);

    /**
     * @return Returns the keyPerson.
     */
    public abstract Integer getKeyPerson();

    /**
     * @param keyPerson
     *            The keyPerson to set.
     */
    public abstract void setKeyPerson(Integer keyPerson);

    /**
     * @return Returns the person.
     */
    public abstract IPessoa getPerson();

    /**
     * @param person
     *            The person to set.
     */
    public abstract void setPerson(IPessoa person);

    /**
     * @return Returns the sendDate.
     */
    public abstract Date getSendDate();

    /**
     * @param sendDate
     *            The sendDate to set.
     */
    public abstract void setSendDate(Date sendDate);
}