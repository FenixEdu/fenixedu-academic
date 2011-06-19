package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramUnit;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

abstract public class PhdEvent extends PhdEvent_Base {

    protected PhdEvent() {
	super();
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
	return getUnit().getAccountBy(AccountType.INTERNAL);
    }

    protected PhdProgramUnit getUnit() {
	return getPhdProgram().getPhdProgramUnit();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
	return new LabelFormatter().appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(
		getPhdProgram().getName().getContent()).appendLabel(")");
    }

    abstract protected PhdProgram getPhdProgram();

    @Override
    public PostingRule getPostingRule() {
	return getPhdProgram().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    public boolean hasPhdEventExemption() {
	return getPhdEventExemption() != null;
    }

    public PhdEventExemption getPhdEventExemption() {
	for (final Exemption exemption : getExemptionsSet()) {
	    if (exemption instanceof PhdEventExemption) {
		return (PhdEventExemption) exemption;
	    }
	}
	return null;
    }

    @Override
    public boolean isPhdEvent() {
	return true;
    }

    public abstract PhdIndividualProgramProcess getPhdIndividualProgramProcess();

    public boolean isPhdThesisRequestFee() {
	return false;
    }
}
