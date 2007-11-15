package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class InternalDegreeChangeEvent extends InternalDegreeChangeEvent_Base {
    
    protected InternalDegreeChangeEvent() {
        super();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	
	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" (p. ");
	//labelFormatter.appendLabel(getDestination().getName());
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(")");
        /*if (getExe hasExecutionYear()) {
            labelFormatter.appendLabel(" - " + getExecutionYear().getYear());    
        }
        */
	
	return labelFormatter;
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public Account getToAccount() {
	return getAdministrativeOffice().getUnit().getAccountBy(AccountType.INTERNAL);
    }
    
    @Override
    public PostingRule getPostingRule() {
	return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(), getWhenOccured());
    }
}
