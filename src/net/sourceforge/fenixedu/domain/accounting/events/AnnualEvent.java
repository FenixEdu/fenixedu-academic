package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public abstract class AnnualEvent extends AnnualEvent_Base {

    protected AnnualEvent() {
	super();
    }

    protected void init(EventType eventType, Person person, ExecutionYear executionYear) {
	init(null, eventType, person, executionYear);
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person,
	    ExecutionYear executionYear) {
	super.init(administrativeOffice, eventType, person);
	checkParameters(executionYear);
	super.setExecutionYear(executionYear);

    }

    private void checkParameters(ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException("error.accounting.events.AnnualEvent.executionYear.cannot.be.null");
	}

    }

    public DateTime getStartDate() {
	return getExecutionYear().getBeginDateYearMonthDay().toDateTimeAtMidnight();

    }

    public DateTime getEndDate() {
	return getExecutionYear().getEndDateYearMonthDay().toDateTimeAtMidnight();
    }

    @Override
    public PostingRule getPostingRule() {
	return getServiceAgreementTemplate().findPostingRuleBy(getEventType(), getStartDate(),
		getEndDate());
    }
    
    public boolean isFor(final ExecutionYear executionYear) {
        return super.getExecutionYear() == executionYear;
    }

    abstract protected ServiceAgreementTemplate getServiceAgreementTemplate();

}
