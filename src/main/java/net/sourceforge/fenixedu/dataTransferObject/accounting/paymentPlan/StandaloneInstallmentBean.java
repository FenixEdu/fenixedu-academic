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
package net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.Installment;

public class StandaloneInstallmentBean extends InstallmentBean {

    private static final long serialVersionUID = -5788931562056516726L;

    private BigDecimal ectsForYear;
    private BigDecimal gratuityFactor;
    private BigDecimal ectsFactor;

    public StandaloneInstallmentBean(Installment installment) {
        super(installment);
    }

    public StandaloneInstallmentBean(PaymentPlanBean paymentPlanBean) {
        super(paymentPlanBean);
    }

    public BigDecimal getEctsForYear() {
        return ectsForYear;
    }

    public void setEctsForYear(BigDecimal ectsForYear) {
        this.ectsForYear = ectsForYear;
    }

    public BigDecimal getGratuityFactor() {
        return gratuityFactor;
    }

    public void setGratuityFactor(BigDecimal gratuityFactor) {
        this.gratuityFactor = gratuityFactor;
    }

    public BigDecimal getEctsFactor() {
        return ectsFactor;
    }

    public void setEctsFactor(BigDecimal ectsFactor) {
        this.ectsFactor = ectsFactor;
    }

    @Override
    public boolean hasRequiredInformation() {
        return (getStartDate() != null) && (ectsFactor != null) && (gratuityFactor != null) && (ectsFactor != null);
    }

}
