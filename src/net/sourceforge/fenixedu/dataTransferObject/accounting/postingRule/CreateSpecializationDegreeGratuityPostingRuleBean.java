package net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

public class CreateSpecializationDegreeGratuityPostingRuleBean extends CreatePostingRuleBean {

    /**
     * 
     */
    private static final long serialVersionUID = 3534703481863625938L;

    private Money amountPerEctsCredit;

    private BigDecimal partialAcceptedPercentage;

    private Money totalAmount;

    public CreateSpecializationDegreeGratuityPostingRuleBean(ServiceAgreementTemplate serviceAgreementTemplate) {
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
