/*
 * Created on 6/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.List;

import org.apache.struts.util.MessageResources;

/**
 * @author Tânia Pousão
 *  
 */
public class PaymentPhase extends PaymentPhase_Base {
    private Date startDate;

    private Date endDate;
	
    private List transactionList;

    /**
     * @return Returns the description.
     */
    public String getDescription() {

        MessageResources messages = MessageResources
                .getMessageResources("ServidorApresentacao.ApplicationResources");

        String newDescription = null;
        newDescription = messages.getMessage(super.getDescription());
        if (newDescription == null) {
            newDescription = super.getDescription();
        }
        return newDescription;
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

    public String toString() {
        StringBuffer object = new StringBuffer();
        object = object.append("\n[PaymentPhase: ").append("idInternal= ").append(getIdInternal())
                .append(" starDate= ").append(startDate).append("; endDate= ").append(endDate).append(
                        "; value= ").append(getValue()).append("; description= ").append(getDescription()).append(
                        "\n");

        return object.toString();
    }

    public boolean equals(Object object) {
        //TODO: to make
        return true;
    }
}