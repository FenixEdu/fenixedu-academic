package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

public class GrantOrientationTeacher extends GrantOrientationTeacher_Base {

    final static Comparator<GrantOrientationTeacher> BEGIN_DATE_COMPARATOR = new BeanComparator("beginDate");

    public GrantOrientationTeacher() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeRootDomainObject();
	removeGrantContract();
	removeOrientationTeacher();
	super.deleteDomainObject();
    }

	@Deprecated
	public java.util.Date getBeginDate(){
		org.joda.time.YearMonthDay ymd = getBeginDateYearMonthDay();
		return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
	}

	@Deprecated
	public void setBeginDate(java.util.Date date){
		if(date == null) setBeginDateYearMonthDay(null);
		else setBeginDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
	}

	@Deprecated
	public java.util.Date getEndDate(){
		org.joda.time.YearMonthDay ymd = getEndDateYearMonthDay();
		return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
	}

	@Deprecated
	public void setEndDate(java.util.Date date){
		if(date == null) setEndDateYearMonthDay(null);
		else setEndDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
	}


}
