package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class ResidenceEvent extends ResidenceEvent_Base {
    
    protected ResidenceEvent() {
        super();
    }

    public ResidenceEvent(ResidenceMonth month, Person person) {
	init(EventType.RESIDENCE_PAYMENT, person, month);
    }
    
    protected void init(EventType eventType, Person person, ResidenceMonth month) {	
	super.init(eventType, person);
	if (month == null) {
	    throw new DomainException("error.accounting.events.ResidenceEvent.ResidenceMonth.cannot.be.null");
	}
	setResidenceMonth(month);
    }
    
    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();

	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" - " + getResidenceMonth().getMonth() + "-" + getResidenceMonth().getYear().getYear());
	return labelFormatter;
    }

    @Override
    protected Account getFromAccount() {
	return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public PostingRule getPostingRule() {
	return getManagementUnit().getUnitServiceAgreementTemplate().findPostingRuleBy(getEventType(), getWhenOccured(), null);
    }

    @Override
    public Account getToAccount() {
	return getManagementUnit().getAccountBy(AccountType.INTERNAL);
    }
    
    
    public ResidenceManagementUnit getManagementUnit() {
	return getResidenceMonth().getManagementUnit();
    }
    
}
