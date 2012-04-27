package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantContractMovement extends GrantContractMovement_Base {

    public GrantContractMovement() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeRootDomainObject();
	removeGrantContract();
	super.deleteDomainObject();
    }


	@Deprecated
	public java.util.Date getArrivalDate(){
		org.joda.time.YearMonthDay ymd = getArrivalDateYearMonthDay();
		return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
	}

	@Deprecated
	public void setArrivalDate(java.util.Date date){
		if(date == null) setArrivalDateYearMonthDay(null);
		else setArrivalDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
	}

	@Deprecated
	public java.util.Date getDepartureDate(){
		org.joda.time.YearMonthDay ymd = getDepartureDateYearMonthDay();
		return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
	}

	@Deprecated
	public void setDepartureDate(java.util.Date date){
		if(date == null) setDepartureDateYearMonthDay(null);
		else setDepartureDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
	}


}
