package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.ServiceExemptionType;

import org.joda.time.YearMonthDay;

public class ServiceExemption extends ServiceExemption_Base {
    
    public ServiceExemption() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }
    
    public void init(YearMonthDay beginDate, YearMonthDay endDate, ServiceExemptionType type, String institution) {
	setType(type);
	setInstitution(institution);
	setOccupationInterval(beginDate, endDate);
    }
    
    @Override
    public void setStartYearMonthDay(YearMonthDay beginDate) {
	checkBeginDateAndEndDate(beginDate, getEndYearMonthDay());
	super.setStartYearMonthDay(beginDate);
    }

    @Override
    public void setEndYearMonthDay(YearMonthDay endDate) {
	checkBeginDateAndEndDate(getStartYearMonthDay(), endDate);
	super.setEndYearMonthDay(endDate);
    }

    @Override
    public void setType(ServiceExemptionType type) {
	if (type == null) {
	    throw new DomainException("error.serviceExemption.no.type");
	}
	super.setType(type);
    }
    
    public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
	return ((endDate == null || !getStartYearMonthDay().isAfter(endDate))
		&& (getEndYearMonthDay() == null || !getEndYearMonthDay().isBefore(beginDate)));
    }

    public void delete() {	
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    public void setOccupationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
	checkBeginDateAndEndDate(beginDate, endDate);
	super.setStartYearMonthDay(beginDate);
	super.setEndYearMonthDay(endDate);
    }
        
    private void checkBeginDateAndEndDate(YearMonthDay beginDate, YearMonthDay endDate) {
	if (beginDate == null) {
	    throw new DomainException("error.serviceExemption.no.beginDate");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.serviceExemption.endDateBeforeBeginDate");
	}
    }
}
