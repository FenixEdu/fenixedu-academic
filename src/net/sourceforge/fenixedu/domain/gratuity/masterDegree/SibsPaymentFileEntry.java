/*
 * Created on Apr 22, 2004
 */
package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

import java.sql.Timestamp;
import java.util.Date;

import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFileEntry extends SibsPaymentFileEntry_Base {

    public SibsPaymentFileEntry() {
    }

    /**
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
        this.setYear(year);
        this.setStudentNumber(studentNumber);
        this.setPaymentType(paymentType);
        this.setTransactionDate(transactionDate);
        this.setPayedValue(payedValue);
        this.setSibsPaymentFile(sibsPaymentFile);
        this.setPaymentStatus(paymentStatus);
    }

    /**
     * @return Returns the transactionDate.
     */
    public Timestamp getTransactionDate() {
        if (this.getTransaction() != null) {
            return new Timestamp(this.getTransaction().getTime());
        }
        return null;
    }

    /**
     * @param transactionDate
     *            The transactionDate to set.
     */
    public void setTransactionDate(Timestamp transactionDate) {
        if (transactionDate != null) {
            this.setTransaction(new Date(transactionDate.getTime()));
        } else {
            this.setTransaction(null);
        }
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "payedValue = " + getPayedValue() + "; \n";
        result += "paymentType = " + this.getPaymentType() + "; \n";
        result += "paymentStatus = " + this.getPaymentStatus() + "; \n";
        result += "sibsPaymentFile = " + getSibsPaymentFile().toString() + "; \n";
        result += "studentNumber = " + getStudentNumber() + "; \n";
        result += "transactionDate = " + this.getTransactionDate().toString() + "; \n";
        result += "year = " + getYear() + "; \n";
        result += "] \n";

        return result;
    }

}
