package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;

public class DeletePostingRule extends Service {

    public void run(final PostingRule postingRule) {
	postingRule.delete();
    }

}
