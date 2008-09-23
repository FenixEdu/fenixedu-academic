package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;

public class DeletePostingRule extends FenixService {

    public void run(final PostingRule postingRule) {
	postingRule.delete();
    }

}
