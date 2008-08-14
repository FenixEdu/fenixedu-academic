package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.PaymentPhase;

/**
 * @author Fernanda Quitério 10/Jan/2004
 * 
 */
public class InfoPaymentPhase extends InfoObject {
    private Date startDate;

    private Date endDate;

    private Double value;

    private String description;

    private InfoGratuityValues infoGratuityValues;

    private List transactionList;

    public String toString() {
	StringBuilder object = new StringBuilder();
	object = object.append("\n[InfoPaymentPhase: ").append("idInternal= ").append(getIdInternal()).append(" starDate= ")
		.append(startDate).append("; endDate= ").append(endDate).append("; value= ").append(value).append(
			"; description= ").append(description).append("\n");

	return object.toString();
    }

    /**
     * @return Returns the transactionList.
     */
    public List getTransactionList() {
	return transactionList;
    }

    /**
     * @param transactionList
     *            The transactionList to set.
     */
    public void setTransactionList(List transactionList) {
	this.transactionList = transactionList;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return this.description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @return Returns the endDate.
     */
    public Date getEndDate() {
	return endDate;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    /**
     * @return Returns the infoGratuityValues.
     */
    public InfoGratuityValues getInfoGratuityValues() {
	return infoGratuityValues;
    }

    /**
     * @param infoGratuityValues
     *            The infoGratuityValues to set.
     */
    public void setInfoGratuityValues(InfoGratuityValues infoGratuityValues) {
	this.infoGratuityValues = infoGratuityValues;
    }

    /**
     * @return Returns the startDate.
     */
    public Date getStartDate() {
	return startDate;
    }

    /**
     * @param startDate
     *            The startDate to set.
     */
    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    /**
     * @return Returns the value.
     */
    public Double getValue() {
	return value;
    }

    /**
     * @param value
     *            The value to set.
     */
    public void setValue(Double value) {
	this.value = value;
    }

    public static Object newInfoFromDoamin(PaymentPhase paymentPhase) {
	InfoPaymentPhase infoPaymentPhase = new InfoPaymentPhase();
	infoPaymentPhase.setDescription(paymentPhase.getDescription());
	infoPaymentPhase.setEndDate(paymentPhase.getEndDate());
	infoPaymentPhase.setIdInternal(paymentPhase.getIdInternal());
	infoPaymentPhase.setStartDate(paymentPhase.getStartDate());
	infoPaymentPhase.setValue(paymentPhase.getValue());

	InfoGratuityValues infoGratuityValues = InfoGratuityValues.newInfoFromDomain(paymentPhase.getGratuityValues());
	infoPaymentPhase.setInfoGratuityValues(infoGratuityValues);

	return infoPaymentPhase;
    }

    public boolean equals(Object obj) {
	return (this == obj);
    }

}