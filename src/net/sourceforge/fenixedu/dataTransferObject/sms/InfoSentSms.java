/*
 * Created on 11/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.sms;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.sms.ISentSms;
import net.sourceforge.fenixedu.util.SmsDeliveryType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class InfoSentSms extends InfoObject {

    protected Integer destinationNumber;

    protected Date sendDate;

    protected Date deliveryDate;

    protected SmsDeliveryType deliveryType;

    protected InfoPerson person;

    /**
     *  
     */
    public InfoSentSms() {
        super();
    }

    /**
     * @param idInternal
     */
    public InfoSentSms(Integer idInternal) {
        super(idInternal);
    }

    /**
     * @return Returns the deliveryDate.
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate
     *            The deliveryDate to set.
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * @return Returns the deliveryType.
     */
    public SmsDeliveryType getDeliveryType() {
        return deliveryType;
    }

    /**
     * @param deliveryType
     *            The deliveryType to set.
     */
    public void setDeliveryType(SmsDeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    /**
     * @return Returns the destinationNumber.
     */
    public Integer getDestinationNumber() {
        return destinationNumber;
    }

    /**
     * @param destinationNumber
     *            The destinationNumber to set.
     */
    public void setDestinationNumber(Integer destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    /**
     * @return Returns the person.
     */
    public InfoPerson getPerson() {
        return person;
    }

    /**
     * @param person
     *            The person to set.
     */
    public void setPerson(InfoPerson person) {
        this.person = person;
    }

    /**
     * @return Returns the sendDate.
     */
    public Date getSendDate() {
        return sendDate;
    }

    /**
     * @param sendDate
     *            The sendDate to set.
     */
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public static InfoSentSms copyFromDomain(ISentSms sentSms) {
        InfoSentSms infoSentSms = null;
        if (sentSms != null) {
            infoSentSms = new InfoSentSms();
            infoSentSms.setIdInternal(sentSms.getIdInternal());
            infoSentSms.setDestinationNumber(sentSms.getDestinationNumber());
            infoSentSms.setSendDate(sentSms.getSendDate());
            infoSentSms.setDeliveryDate(sentSms.getDeliveryDate());
            infoSentSms.setDeliveryType(sentSms.getDeliveryType());
        }

        return infoSentSms;
    }

}