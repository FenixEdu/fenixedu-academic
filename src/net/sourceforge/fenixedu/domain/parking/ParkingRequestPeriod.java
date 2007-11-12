package net.sourceforge.fenixedu.domain.parking;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class ParkingRequestPeriod extends ParkingRequestPeriod_Base {

    public ParkingRequestPeriod(DateTime beginDateTime, DateTime endDateTime) {
	super();

	DateTime now = new DateTime();
	if (beginDateTime == null) {
	    beginDateTime = now;
	}
	if (endDateTime == null) {
	    endDateTime = now;
	}
	if (!beginDateTime.isBefore(endDateTime)) {
	    throw new DomainException("error.beginDateBeforeOrEqualEndDate");
	}
	setBeginDate(beginDateTime);
	setEndDate(endDateTime);
	Interval thisInterval = getRequestPeriodInterval();
	for (ParkingRequestPeriod parkingRequestPeriod : RootDomainObject.getInstance()
		.getParkingRequestPeriods()) {
	    if (parkingRequestPeriod.getRequestPeriodInterval().overlaps(thisInterval)) {
		throw new DomainException("error.overlapsAnotherInterval");
	    }
	}
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void editParkingRequestPeriod(DateTime beginDateTime, DateTime endDateTime) {
	DateTime now = new DateTime();
	if (beginDateTime == null) {
	    beginDateTime = now;
	}
	if (endDateTime == null) {
	    endDateTime = now;
	}
	if (!beginDateTime.isBefore(endDateTime)) {
	    throw new DomainException("error.beginDateBeforeOrEqualEndDate");
	}
	Interval thisInterval = new Interval(beginDateTime, endDateTime);
	for (ParkingRequestPeriod parkingRequestPeriod : RootDomainObject.getInstance()
		.getParkingRequestPeriods()) {
	    if ((!parkingRequestPeriod.getIdInternal().equals(getIdInternal()))
		    && parkingRequestPeriod.getRequestPeriodInterval().overlaps(thisInterval)) {
		throw new DomainException("error.overlapsAnotherInterval");
	    }
	}
	setBeginDate(beginDateTime);
	setEndDate(endDateTime);
    }

    public Interval getRequestPeriodInterval() {
	return new Interval(getBeginDate(), getEndDate());
    }

    public static boolean isDateInAnyRequestPeriod(DateTime date) {
	for (ParkingRequestPeriod parkingRequestPeriod : RootDomainObject.getInstance()
		.getParkingRequestPeriods()) {
	    if (new Interval(parkingRequestPeriod.getBeginDate(), parkingRequestPeriod.getEndDate())
		    .contains(date)) {
		return Boolean.TRUE;
	    }
	}
	return Boolean.FALSE;
    }

    public void delete() {
	removeRootDomainObject();
	deleteDomainObject();
    }

}
