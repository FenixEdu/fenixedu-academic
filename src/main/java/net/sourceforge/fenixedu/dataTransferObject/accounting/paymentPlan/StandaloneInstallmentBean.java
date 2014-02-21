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
