package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdProgramCandidacyEvent extends PhdProgramCandidacyEvent_Base {

    public PhdProgramCandidacyEvent() {
	super();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	throw new RuntimeException("Not implemented");
    }

    @Override
    protected Account getFromAccount() {
	throw new RuntimeException("Not implemented");
    }

    @Override
    public PostingRule getPostingRule() {
	throw new RuntimeException("Not implemented");
    }

    @Override
    public Account getToAccount() {
	throw new RuntimeException("Not implemented");
    }

}
