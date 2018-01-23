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
package org.fenixedu.academic.dto.accounting.postingRule;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.EnrolmentGratuityPR;

public class CreateEnrolmentGratuityPRBean extends CreateGratuityPostingRuleBean {

    static private final long serialVersionUID = 1L;

    private BigDecimal amountPerEcts;
    private Integer numberOfDaysToStartApplyingInterest;
    private EventType eventType;
    private boolean forAliens;

    public CreateEnrolmentGratuityPRBean() {
        super();
        super.setRule(EnrolmentGratuityPR.class);
    }

    public BigDecimal getAmountPerEcts() {
        return amountPerEcts;
    }

    public void setAmountPerEcts(BigDecimal amountPerEcts) {
        this.amountPerEcts = amountPerEcts;
    }

    public Integer getNumberOfDaysToStartApplyingInterest() {
        return numberOfDaysToStartApplyingInterest;
    }

    public void setNumberOfDaysToStartApplyingInterest(Integer numberOfDaysToStartApplyingInterest) {
        this.numberOfDaysToStartApplyingInterest = numberOfDaysToStartApplyingInterest;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public boolean isForAliens() {
        return forAliens;
    }

    public void setForAliens(boolean forAliens) {
        this.forAliens = forAliens;
    }
}
