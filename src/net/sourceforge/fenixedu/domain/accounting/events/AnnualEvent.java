package net.sourceforge.fenixedu.domain.accounting.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventState;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class AnnualEvent extends AnnualEvent_Base {

    protected AnnualEvent() {
	super();
    }

    protected void init(EventType eventType, Person person, ExecutionYear executionYear) {
	init(null, eventType, person, executionYear);
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person, ExecutionYear executionYear) {
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
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException("error.accounting.events.AnnualEvent.cannot.modify.executionYear");
    }

    @Override
    public PostingRule getPostingRule() {
	return getServiceAgreementTemplate().findPostingRuleBy(getEventType(), getStartDate(), getEndDate());
    }

    public boolean isFor(final ExecutionYear executionYear) {
	return super.getExecutionYear() == executionYear;
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Override
    public void delete() {

	super.setExecutionYear(null);

	super.delete();
    }

    @Override
    public boolean isAnnual() {
	return true;
    }

    private static List<AnnualEvent> readBy(final ExecutionYear executionYear, final EventState eventState) {
	final List<AnnualEvent> result = new ArrayList<AnnualEvent>();
	for (final Event event : RootDomainObject.getInstance().getAccountingEventsSet()) {
	    if (event.isAnnual() && event.isInState(eventState) && ((AnnualEvent) event).getExecutionYear() == executionYear) {
		result.add((AnnualEvent) event);
	    }
	}

	return result;
    }

    public static List<AnnualEvent> readNotPayedBy(final ExecutionYear executionYear) {
	return readBy(executionYear, EventState.OPEN);
    }

    static public Set<AccountingTransaction> readPaymentsFor(final Class<? extends AnnualEvent> eventClass,
	    final YearMonthDay startDate, final YearMonthDay endDate) {
	final Set<AccountingTransaction> result = new HashSet<AccountingTransaction>();
	for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYears()) {
	    for (final AnnualEvent each : executionYear.getAnnualEvents()) {
		if (eventClass.equals(each.getClass()) && !each.isCancelled()) {
		    for (final AccountingTransaction transaction : each.getNonAdjustingTransactions()) {
			if (transaction.isInsidePeriod(startDate, endDate)) {
			    result.add(transaction);
			}
		    }
		}
	    }
	}

	return result;
    }

    abstract protected ServiceAgreementTemplate getServiceAgreementTemplate();

}
