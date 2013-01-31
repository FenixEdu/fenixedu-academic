package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class ExternalScholarshipPhdGratuityContribuitionEvent extends ExternalScholarshipPhdGratuityContribuitionEvent_Base {
	public ExternalScholarshipPhdGratuityContribuitionEvent(Party party) {
		super();
		init(EventType.EXTERNAL_SCOLARSHIP, party);
		setRootDomainObject(RootDomainObject.getInstance());
	}

	@Override
	protected void disconnect() {
		PhdGratuityExternalScholarshipExemption exemption = getPhdGratuityExternalScholarshipExemption();
		exemption.doDelete();
		super.disconnect();
	}

	public Money calculateAmountToPay() {
		return calculateAmountToPay(new DateTime());
	}

	public Money getTotalValue() {
		return getPhdGratuityExternalScholarshipExemption().getValue();
	}

	@Override
	protected Account getFromAccount() {
		return getParty().getAccountBy(AccountType.EXTERNAL);
	}

	@Override
	public Account getToAccount() {
		return ((PhdGratuityEvent) getPhdGratuityExternalScholarshipExemption().getEvent()).getPhdProgram().getPhdProgramUnit()
				.getAccountBy(AccountType.INTERNAL);
	}

	@Override
	public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
		return new LabelFormatter()
				.appendLabel(entryType.name(), "enum")
				.appendLabel(" (")
				.appendLabel(
						((PhdGratuityEvent) getPhdGratuityExternalScholarshipExemption().getEvent()).getPhdProgram().getName()
								.getContent()).appendLabel(")");
	}

	@Override
	public PostingRule getPostingRule() {
		return AdministrativeOffice.readMasterDegreeAdministrativeOffice().getServiceAgreementTemplate()
				.findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
	}

	@Override
	public Unit getOwnerUnit() {
		return AdministrativeOffice.readMasterDegreeAdministrativeOffice().getUnit();
	}
}