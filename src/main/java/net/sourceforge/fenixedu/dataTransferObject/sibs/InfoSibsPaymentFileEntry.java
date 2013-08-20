package net.sourceforge.fenixedu.dataTransferObject.sibs;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InfoSibsPaymentFileEntry extends InfoObject {

    private Integer year;

    private Integer studentNumber;

    private SibsPaymentType paymentType;

    private Timestamp transactionDate;

    private Double payedValue;

    private SibsPaymentStatus paymentStatus;

    /**
     *  
     */
    public InfoSibsPaymentFileEntry() {
        super();
    }

    /**
     * @param externalId
     */
    public InfoSibsPaymentFileEntry(String externalId) {
        super(externalId);
    }

    public Double getPayedValue() {
        return payedValue;
    }

    public void setPayedValue(Double payedValue) {
        this.payedValue = payedValue;
    }

    public SibsPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(SibsPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public SibsPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(SibsPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void copyFromDomain(SibsPaymentFileEntry sibsPaymentFileEntry) {

        setPayedValue(sibsPaymentFileEntry.getPayedValue());
        setPaymentStatus(sibsPaymentFileEntry.getPaymentStatus());
        setPaymentType(sibsPaymentFileEntry.getPaymentType());
        setStudentNumber(sibsPaymentFileEntry.getStudentNumber());
        if (sibsPaymentFileEntry.getTransactionDate() != null) {
            this.setTransactionDate(new Timestamp(sibsPaymentFileEntry.getTransactionDate().getTime()));
        }
        setYear(sibsPaymentFileEntry.getYear());
        setExternalId(sibsPaymentFileEntry.getExternalId());

    }

    public static InfoSibsPaymentFileEntry newInfoFromDomain(SibsPaymentFileEntry sibsPaymentFileEntry) {
        InfoSibsPaymentFileEntry infoSibsPaymentFileEntry = null;

        if (sibsPaymentFileEntry != null) {
            infoSibsPaymentFileEntry = new InfoSibsPaymentFileEntry();
            infoSibsPaymentFileEntry.copyFromDomain(sibsPaymentFileEntry);
        }

        return infoSibsPaymentFileEntry;
    }
}