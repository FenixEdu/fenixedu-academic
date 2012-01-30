package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu._development.PropertiesManager;
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

public class FctScholarshipPhdGratuityContribuitionEvent extends FctScholarshipPhdGratuityContribuitionEvent_Base {
    public FctScholarshipPhdGratuityContribuitionEvent() {
	super();
	init(EventType.FCT_SCOLARSHIP, Party.readByContributorNumber(PropertiesManager.getProperty("fct.contributor.number")));
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Override
    protected void disconnect() {
	PhdGratuityFctScholarshipExemption exemption = getPhdGratuityFctScholarshipExemption();
	exemption.doDelete();
	super.disconnect();
    }

    public Money calculateAmountToPay() {
	return calculateAmountToPay(new DateTime());
    }

    public Money getTotalValue(){
	return getPhdGratuityFctScholarshipExemption().getValue();
    }
    
    @Override
    protected Account getFromAccount() {
	return getParty().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
	return ((PhdGratuityEvent) getPhdGratuityFctScholarshipExemption().getEvent()).getPhdProgram().getPhdProgramUnit().getAccountBy(AccountType.INTERNAL);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	return new LabelFormatter()
		.appendLabel(entryType.name(), "enum")
		.appendLabel(" (")
		.appendLabel(
			((PhdGratuityEvent) getPhdGratuityFctScholarshipExemption().getEvent()).getPhdProgram().getName()
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

    @Override
    public boolean isFctScholarshipPhdGratuityContribuitionEvent() {
	return true;
    }

}