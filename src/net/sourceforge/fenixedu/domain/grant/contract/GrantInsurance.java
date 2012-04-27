package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantInsurance extends GrantInsurance_Base {

    public GrantInsurance() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeGrantContract();
	removeGrantPaymentEntity();
	removeRootDomainObject();
	super.deleteDomainObject();
    }


	@Deprecated
	public java.util.Date getDateBeginInsurance(){
		org.joda.time.YearMonthDay ymd = getDateBeginInsuranceYearMonthDay();
		return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
	}

	@Deprecated
	public void setDateBeginInsurance(java.util.Date date){
		if(date == null) setDateBeginInsuranceYearMonthDay(null);
		else setDateBeginInsuranceYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
	}

	@Deprecated
	public java.util.Date getDateEndInsurance(){
		org.joda.time.YearMonthDay ymd = getDateEndInsuranceYearMonthDay();
		return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
	}

	@Deprecated
	public void setDateEndInsurance(java.util.Date date){
		if(date == null) setDateEndInsuranceYearMonthDay(null);
		else setDateEndInsuranceYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
	}


}
