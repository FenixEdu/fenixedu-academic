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

import org.fenixedu.academic.domain.accounting.postingRules.gratuity.PartialRegimePR;
import org.fenixedu.academic.util.Money;

public class CreatePartialRegimePRBean extends CreateGratuityPostingRuleBean {

    static private final long serialVersionUID = 1L;

    private Money amount;
    private Integer numberOfDaysToStartApplyingInterest;
    private boolean forAliens;

    public CreatePartialRegimePRBean() {
        super();
        super.setRule(PartialRegimePR.class);
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Integer getNumberOfDaysToStartApplyingInterest() {
        return numberOfDaysToStartApplyingInterest;
    }

    public void setNumberOfDaysToStartApplyingInterest(Integer numberOfDaysToStartApplyingInterest) {
        this.numberOfDaysToStartApplyingInterest = numberOfDaysToStartApplyingInterest;
    }

    public boolean isForAliens() {
        return forAliens;
    }

    public void setForAliens(boolean forAliens) {
        this.forAliens = forAliens;
    }
}
