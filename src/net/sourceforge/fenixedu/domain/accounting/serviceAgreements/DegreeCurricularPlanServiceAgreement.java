package net.sourceforge.fenixedu.domain.accounting.serviceAgreements;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class DegreeCurricularPlanServiceAgreement extends DegreeCurricularPlanServiceAgreement_Base {

    private DegreeCurricularPlanServiceAgreement() {
        super();
    }

    public DegreeCurricularPlanServiceAgreement(Person person,
            DegreeCurricularPlanServiceAgreementTemplate degreeCurricularPlanServiceAgreementTemplate) {
        this();
        super.init(person, degreeCurricularPlanServiceAgreementTemplate);
    }

    @Override
    public DegreeCurricularPlanServiceAgreementTemplate getServiceAgreementTemplate() {
        return (DegreeCurricularPlanServiceAgreementTemplate) super.getServiceAgreementTemplate();
    }

}
