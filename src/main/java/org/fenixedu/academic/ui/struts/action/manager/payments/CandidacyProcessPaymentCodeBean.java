/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.manager.payments;

import java.io.Serializable;

import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.joda.time.LocalDate;

public class CandidacyProcessPaymentCodeBean implements Serializable {

    private PaymentCodeType type;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Integer numberOfPaymentCodes;
    private Double minAmount;
    private Double maxAmount;

    public CandidacyProcessPaymentCodeBean() {

    }

    public CandidacyProcessPaymentCodeBean(PaymentCodeType type) {
        this.type = type;
    }

    public PaymentCodeType getType() {
        return type;
    }

    public void setType(PaymentCodeType type) {
        this.type = type;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getNumberOfPaymentCodes() {
        return numberOfPaymentCodes;
    }

    public void setNumberOfPaymentCodes(Integer numberOfPaymentCodes) {
        this.numberOfPaymentCodes = numberOfPaymentCodes;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

}
