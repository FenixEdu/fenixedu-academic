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
package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;

import org.joda.time.LocalDate;

public class CandidacyProcessPaymentCodeBean implements Serializable {

    private PaymentCodeType type;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Integer numberOfPaymentCodes;

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

}
