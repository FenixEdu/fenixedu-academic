/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    @Override
    public String toString() {
        StringBuilder object = new StringBuilder();
        object =
                object.append("\n[InfoPaymentPhase: ").append("externalId= ").append(getExternalId()).append(" starDate= ")
                        .append(startDate).append("; endDate= ").append(endDate).append("; value= ").append(value)
                        .append("; description= ").append(description).append("\n");

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
        infoPaymentPhase.setExternalId(paymentPhase.getExternalId());
        infoPaymentPhase.setStartDate(paymentPhase.getStartDate());
        infoPaymentPhase.setValue(paymentPhase.getValue());

        InfoGratuityValues infoGratuityValues = InfoGratuityValues.newInfoFromDomain(paymentPhase.getGratuityValues());
        infoPaymentPhase.setInfoGratuityValues(infoGratuityValues);

        return infoPaymentPhase;
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj);
    }

}