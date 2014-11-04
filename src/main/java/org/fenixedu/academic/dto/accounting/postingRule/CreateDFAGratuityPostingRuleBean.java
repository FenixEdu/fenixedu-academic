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

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import org.fenixedu.academic.util.Money;

public class CreateDFAGratuityPostingRuleBean extends CreatePostingRuleBean {

    /**
     * 
     */
    private static final long serialVersionUID = 3534703481863625938L;

    private Money amountPerEctsCredit;

    private BigDecimal partialAcceptedPercentage;

    private Money totalAmount;

    public CreateDFAGratuityPostingRuleBean(ServiceAgreementTemplate serviceAgreementTemplate) {
        super(serviceAgreementTemplate);
    }

    public Money getAmountPerEctsCredit() {
        return amountPerEctsCredit;
    }

    public void setAmountPerEctsCredit(Money amountPerEctsCredit) {
        this.amountPerEctsCredit = amountPerEctsCredit;
    }

    public BigDecimal getPartialAcceptedPercentage() {
        return partialAcceptedPercentage;
    }

    public void setPartialAcceptedPercentage(BigDecimal partialAcceptedPercentage) {
        this.partialAcceptedPercentage = partialAcceptedPercentage;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public DegreeCurricularPlanServiceAgreementTemplate getServiceAgreementTemplate() {
        return (DegreeCurricularPlanServiceAgreementTemplate) super.getServiceAgreementTemplate();
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return getServiceAgreementTemplate().getDegreeCurricularPlan();
    }

}
