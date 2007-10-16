package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateDFAGratuityPostingRuleBean;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityPR;

public class CreatePostingRule extends Service {

    public void run(final CreateDFAGratuityPostingRuleBean createDFAGratuityPostingRuleBean) {

	new DFAGratuityPR(createDFAGratuityPostingRuleBean.getStartDate(), null, createDFAGratuityPostingRuleBean
		.getServiceAgreementTemplate(), createDFAGratuityPostingRuleBean.getTotalAmount(),
		createDFAGratuityPostingRuleBean.getPartialAcceptedPercentage(), createDFAGratuityPostingRuleBean
			.getAmountPerEctsCredit());

    }

}
