/*
 * Created on Apr 22, 2004
 */
package Dominio.gratuity.masterDegree;

import java.sql.Timestamp;

import Dominio.DomainObject;
import Util.gratuity.SibsPaymentStatus;
import Util.gratuity.SibsPaymentType;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFileEntry extends DomainObject implements ISibsPaymentFileEntry {

    private Integer keySibsPaymentFile;

    private Integer year;

    private Integer studentNumber;

    private SibsPaymentType paymentType;

    private Timestamp transactionDate;

    private Double payedValue;

    private ISibsPaymentFile sibsPaymentFile;

    private SibsPaymentStatus paymentStatus;

    public SibsPaymentFileEntry() {
    }

    /**
     * 
     * @param year
     * @param studentNumber
     * @param paymentType
     * @param transactionDate
     * @param payedValue
     * @param sibsPaymentFile
     * @param sibsPaymentStatusType
     */
    public SibsPaymentFileEntry(Integer year, Integer studentNumber, SibsPaymentType paymentType,
            Timestamp transactionDate, Double payedValue, ISibsPaymentFile sibsPaymentFile,
            SibsPaymentStatus paymentStatus) {
        this.year = year;
        this.studentNumber = studentNumber;
        this.paymentType = paymentType;
        this.transactionDate = transactionDate;
        this.payedValue = payedValue;
        this.sibsPaymentFile = sibsPaymentFile;
        this.paymentStatus = paymentStatus;
    }

    /**
     * @return Returns the keySibsFile.
     */
    public Integer getKeySibsPaymentFile() {
        return keySibsPaymentFile;
    }

    /**
     * @param keySibsFile
     *            The keySibsFile to set.
     */
    public void setKeySibsPaymentFile(Integer keySibsPaymentFile) {
        this.keySibsPaymentFile = keySibsPaymentFile;
    }

    /**
     * @return Returns the paymentType.
     */
    public SibsPaymentType getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType
     *            The paymentType to set.
     */
    public void setPaymentType(SibsPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return Returns the sibsFile.
     */
    public ISibsPaymentFile getSibsPaymentFile() {
        return sibsPaymentFile;
    }

    /**
     * @param sibsFile
     *            The sibsFile to set.
     */
    public void setSibsPaymentFile(ISibsPaymentFile sibsPaymentFile) {
        this.sibsPaymentFile = sibsPaymentFile;
    }

    /**
     * @return Returns the studentNumber.
     */
    public Integer getStudentNumber() {
        return studentNumber;
    }

    /**
     * @param studentNumber
     *            The studentNumber to set.
     */
    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    /**
     * @return Returns the transactionDate.
     */
    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate
     *            The transactionDate to set.
     */
    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return Returns the payedValue.
     */
    public Double getPayedValue() {
        return payedValue;
    }

    /**
     * @param payedValue
     *            The payedValue to set.
     */
    public void setPayedValue(Double payedValue) {
        this.payedValue = payedValue;
    }

    /**
     * @return Returns the year.
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year
     *            The year to set.
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return Returns the paymentStatus.
     */
    public SibsPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * @param paymentStatus
     *            The paymentStatus to set.
     */
    public void setPaymentStatus(SibsPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if ((obj instanceof ISibsPaymentFileEntry)) {
            ISibsPaymentFileEntry sibsFileEntry = (ISibsPaymentFileEntry) obj;
            if ((this.getIdInternal() != null) && (sibsFileEntry.getIdInternal() != null)
                    && (this.getIdInternal().equals(sibsFileEntry.getIdInternal()))) {

                result = true;
            }

        }

        return result;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "payedValue = " + this.payedValue + "; \n";
        result += "paymentType = " + this.paymentType + "; \n";
        result += "paymentStatus = " + this.paymentStatus + "; \n";
        result += "sibsPaymentFile = " + this.sibsPaymentFile.toString() + "; \n";
        result += "studentNumber = " + this.studentNumber + "; \n";
        result += "transactionDate = " + this.transactionDate.toString() + "; \n";
        result += "year = " + this.year + "; \n";
        result += "] \n";

        return result;
    }

}