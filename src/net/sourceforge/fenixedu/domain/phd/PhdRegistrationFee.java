package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PhdRegistrationFee extends PhdRegistrationFee_Base {

    private PhdRegistrationFee() {
	super();
    }

    public PhdRegistrationFee(final Person person, final PhdIndividualProgramProcess process) {
	this();
	init(AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE), person, process);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, PhdIndividualProgramProcess process) {
	super.init(administrativeOffice, EventType.PHD_REGISTRATION_FEE, person);
	check(process, "error.PhdRegistrationFee.process.cannot.be.null");
	super.setProcess(process);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
	return new LabelFormatter().appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(
		getPhdProgram().getName().getContent()).appendLabel(")");
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
	return getUnit().getAccountBy(AccountType.INTERNAL);
    }

    private PhdProgram getPhdProgram() {
	return getProcess().getPhdProgram();
    }

    private PhdProgramUnit getUnit() {
	return getPhdProgram().getPhdProgramUnit();
    }

    @Override
    public PostingRule getPostingRule() {
	return getPhdProgram().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }

    @Override
    protected void disconnect() {
	removeProcess();
	super.disconnect();
    }

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    public boolean hasPhdRegistrationFeePenaltyExemption() {
	return getPhdRegistrationFeePenaltyExemption() != null;
    }

    private PhdRegistrationFeePenaltyExemption getPhdRegistrationFeePenaltyExemption() {
	for (final Exemption exemption : getExemptionsSet()) {
	    if (exemption instanceof PhdRegistrationFeePenaltyExemption) {
		return (PhdRegistrationFeePenaltyExemption) exemption;
	    }
	}

	return null;
    }

}
