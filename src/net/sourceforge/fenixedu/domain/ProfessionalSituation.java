package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.RegimeType;

import org.joda.time.YearMonthDay;

public abstract class ProfessionalSituation extends ProfessionalSituation_Base {
    
    public ProfessionalSituation() {
        super();
        setOjbConcreteClass(getClass().getName());
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void init(YearMonthDay beginDate, YearMonthDay endDate, ProfessionalSituationType type, RegimeType regimenType, Employee employee) {
	setOccupationInterval(beginDate, endDate);
	setSituationType(type);
	setRegimeType(regimenType);
	setEmployee(employee);
    }
    
    @Override
    public void setBeginDateYearMonthDay(YearMonthDay beginDate) {
	checkBeginDateAndEndDate(beginDate, getEndDateYearMonthDay());	
	super.setBeginDateYearMonthDay(beginDate);
    }

    @Override
    public void setEndDateYearMonthDay(YearMonthDay endDate) {
	checkBeginDateAndEndDate(getBeginDateYearMonthDay(), endDate);	
	super.setEndDateYearMonthDay(endDate);
    }
    
    public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
	return ((endDate == null || !getBeginDateYearMonthDay().isAfter(endDate)) 
		&& (getEndDateYearMonthDay() == null || !getEndDateYearMonthDay().isBefore(beginDate)));
    }

    public boolean isActive(YearMonthDay currentDate) {
	return belongsToPeriod(currentDate, currentDate);
    }

    public void setOccupationInterval(YearMonthDay beginDate, YearMonthDay endDate) {
	checkBeginDateAndEndDate(beginDate, endDate);	
	super.setBeginDateYearMonthDay(beginDate);
	super.setEndDateYearMonthDay(endDate);
    }
    
    @Override
    public void setSituationType(ProfessionalSituationType situationType) {
	if(situationType == null) {
	    throw new DomainException("error.ProfessionalSituation.empty.situationType");
	}
	super.setSituationType(situationType);
    }
    
    @Override
    public void setEmployee(Employee employee) {
        if(employee == null) {
            throw new DomainException("error.ProfessionalSituation.empty.employee");
        }
	super.setEmployee(employee);
    }
    
    public void delete() {
	super.setEmployee(null);
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    private void checkBeginDateAndEndDate(YearMonthDay beginDate, YearMonthDay endDate) {
	if (beginDate == null) {
	    throw new DomainException("error.ProfessionalSituation.no.beginDate");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.ProfessionalSituation.endDateBeforeBeginDate");
	}
    }
    
    public boolean isTeacherProfessionalSituation() {
	return false;
    }
    
    public boolean isTeacherServiceExemption() {
	return false;
    }
    
    public boolean isEmployeeProfessionalSituation() {
	return false;
    }
}
