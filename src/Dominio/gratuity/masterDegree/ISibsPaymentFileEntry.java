/*
 * Created on Apr 29, 2004
 *
 */
package Dominio.gratuity.masterDegree;

import java.sql.Timestamp;

import Dominio.IDomainObject;
import Util.gratuity.SibsPaymentStatus;
import Util.gratuity.SibsPaymentType;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public interface ISibsPaymentFileEntry extends IDomainObject {

    /**
     * @return Returns the keyPaymentSibsFile.
     */
    public abstract Integer getKeySibsPaymentFile();

    /**
     * @param keySibsPaymentFile
     *            The keyPaymentSibsFile to set.
     */
    public abstract void setKeySibsPaymentFile(Integer keySibsPaymentFile);

    /**
     * @return Returns the paymentType.
     */
    public SibsPaymentType getPaymentType();

    /**
     * @param paymentType
     *            The paymentType to set.
     */
    public void setPaymentType(SibsPaymentType paymentType);

    /**
     * @return Returns the sibsFile.
     */
    public abstract ISibsPaymentFile getSibsPaymentFile();

    /**
     * @param sibsFile
     *            The sibsFile to set.
     */
    public abstract void setSibsPaymentFile(ISibsPaymentFile sibsPaymentFile);

    /**
     * @return Returns the studentNumber.
     */
    public abstract Integer getStudentNumber();

    /**
     * @param studentNumber
     *            The studentNumber to set.
     */
    public abstract void setStudentNumber(Integer studentNumber);

    /**
     * @return Returns the transactionDate.
     */
    public abstract Timestamp getTransactionDate();

    /**
     * @param transactionDate
     *            The transactionDate to set.
     */
    public abstract void setTransactionDate(Timestamp transactionDate);

    /**
     * @return Returns the payedValue.
     */
    public abstract Double getPayedValue();

    /**
     * @param value
     *            The value to set.
     */
    public abstract void setPayedValue(Double value);

    /**
     * @return Returns the year.
     */
    public abstract Integer getYear();

    /**
     * @param year
     *            The year to set.
     */
    public abstract void setYear(Integer year);

    /**
     * @return Returns the paymentStatus.
     */
    public SibsPaymentStatus getPaymentStatus();

    /**
     * @param paymentStatus
     *            The paymentStatus to set.
     */
    public void setPaymentStatus(SibsPaymentStatus paymentStatus);

}