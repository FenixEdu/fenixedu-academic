package net.sourceforge.fenixedu.domain.residence;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class ResidenceMonth extends ResidenceMonth_Base {

    protected ResidenceMonth() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ResidenceMonth(Month month, ResidenceYear residenceYear) {
	this();
	setMonth(month);
	setYear(residenceYear);
    }

    public ResidenceManagementUnit getManagementUnit() {
	return getYear().getUnit();
    }

    public boolean isEventPresent(Person person) {
	for (ResidenceEvent event : getEvents()) {
	    if (event.getPerson() == person && (event.isOpen() || event.isPayed())) {
		return true;
	    }
	}
	return false;
    }

    public DateTime getPaymentStartDate() {
	LocalDate date = new LocalDate(getYear().getYear(), getMonth().getNumberOfMonth(), 1);
	return date.toDateTimeAtStartOfDay();
    }

    public DateTime getPaymentLimitDateTime() {
	ResidenceYear residenceYear = getYear();
	LocalDate date = new LocalDate(residenceYear.getYear(), getMonth().getNumberOfMonth(), getManagementUnit()
		.getCurrentPaymentLimitDay());
	return date.toDateTimeAtStartOfDay();
    }

    public boolean isAbleToEditPaymentLimitDate() {
	return getEventsCount() == 0;
    }

    public Set<ResidenceEvent> getEventsWithPaymentCodes() {
	Set<ResidenceEvent> eventsWithCodes = new HashSet<ResidenceEvent>();

	for (ResidenceEvent event : getEvents()) {
	    if (event.getPaymentCodesCount() > 0 && !event.isCancelled()) {
		eventsWithCodes.add(event);
	    }
	}
	return eventsWithCodes;
    }

    public Set<ResidenceEvent> getEventsWithoutPaymentCodes() {
	Set<ResidenceEvent> eventsWithoutCodes = new HashSet<ResidenceEvent>();

	for (ResidenceEvent event : getEvents()) {
	    if (event.getPaymentCodesCount() == 0 && !event.isCancelled()) {
		eventsWithoutCodes.add(event);
	    }
	}
	return eventsWithoutCodes;
    }
}
