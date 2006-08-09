package net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeCurricularPlanServiceAgreementTemplate extends DegreeCurricularPlanServiceAgreementTemplate_Base {

    private DegreeCurricularPlanServiceAgreementTemplate() {
        super();
    }

    public DegreeCurricularPlanServiceAgreementTemplate(DegreeCurricularPlan degreeCurricularPlan) {
        this();
        init(degreeCurricularPlan);
    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan) {
        if (degreeCurricularPlan == null) {
            throw new DomainException(
                    "error.accounting.agreement.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate.degreeCurricularPlan.cannot.be.null");
        }

    }

    protected void init(DegreeCurricularPlan degreeCurricularPlan) {
        checkParameters(degreeCurricularPlan);
        super.setDegreeCurricularPlan(degreeCurricularPlan);
    }

    @Override
    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        throw new DomainException(
                "error.accounting.agreement.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate.cannot.modify.degreeCurricularPlan");
    }

}
