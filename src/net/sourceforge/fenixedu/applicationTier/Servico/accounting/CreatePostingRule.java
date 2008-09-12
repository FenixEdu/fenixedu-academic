package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateDFAGratuityPostingRuleBean;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByNumberOfEnrolmentsPR;

public class CreatePostingRule extends Service {

    public void run(final CreateDFAGratuityPostingRuleBean bean) {
	if (bean.getRule() == DFAGratuityByAmountPerEctsPR.class) {
	    new DFAGratuityByAmountPerEctsPR(bean.getStartDate(), null, bean.getServiceAgreementTemplate(),
		    bean.getTotalAmount(), bean.getPartialAcceptedPercentage(), bean.getAmountPerEctsCredit());
	} else {
	    new DFAGratuityByNumberOfEnrolmentsPR(bean.getStartDate(), null, bean.getServiceAgreementTemplate(), bean
		    .getTotalAmount(), bean.getPartialAcceptedPercentage());
	}
    }

}
