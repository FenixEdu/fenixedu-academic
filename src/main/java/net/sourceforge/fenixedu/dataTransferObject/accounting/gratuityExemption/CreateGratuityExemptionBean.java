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
package net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionJustificationType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

public class CreateGratuityExemptionBean implements Serializable {

    private GratuityEvent gratuityEvent;

    private GratuityExemptionJustificationType exemptionJustificationType;

    private String reason;

    private YearMonthDay dispatchDate;

    private BigDecimal otherPercentage;

    private Money amount;

    private BigDecimal percentage;

    public CreateGratuityExemptionBean(GratuityEvent gratuityEvent) {
        super();
        setGratuityEvent(gratuityEvent);
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public GratuityExemptionJustificationType getExemptionJustificationType() {
        return exemptionJustificationType;
    }

    public void setExemptionJustificationType(GratuityExemptionJustificationType exemptionType) {
        this.exemptionJustificationType = exemptionType;
    }

    public GratuityEvent getGratuityEvent() {
        return (gratuityEvent != null) ? this.gratuityEvent : null;
    }

    public void setGratuityEvent(GratuityEvent gratuityEvent) {
        this.gratuityEvent = gratuityEvent;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getOtherPercentage() {
        return otherPercentage;
    }

    public void setOtherPercentage(BigDecimal selectedPercentage) {
        this.otherPercentage = selectedPercentage;
    }

    public boolean isPercentageExemption() {
        return getOtherPercentage() != null || getPercentage() != null;
    }

    public BigDecimal getSelectedPercentage() {
        return getOtherPercentage() != null ? getOtherPercentage() : getPercentage();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public YearMonthDay getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(YearMonthDay dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

}
