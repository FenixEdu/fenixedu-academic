package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LegalRegimenType;
import net.sourceforge.fenixedu.util.RegimenType;

import org.joda.time.YearMonthDay;

public class LegalRegimen extends LegalRegimen_Base {
    
    public LegalRegimen() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }
    
    public void init(YearMonthDay beginDate, YearMonthDay endDate, LegalRegimenType legalRegimenType, RegimenType regimenType) {
	setOccupationInterval(beginDate, endDate);
	setLegalRegimenType(legalRegimenType);
	setRegimenType(regimenType);
    }
    
    @Override
    public void setLegalRegimenType(LegalRegimenType legalRegimenType) {
	if (legalRegimenType == null) {
	    throw new DomainException("error.LegalRegimen.no.legalRegimenType");
	}
	super.setLegalRegimenType(legalRegimenType);
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
    
    public void delete() {
	removeRootDomainObject();
	deleteDomainObject();
    }
    
    private void checkBeginDateAndEndDate(YearMonthDay beginDate, YearMonthDay endDate) {
	if (beginDate == null) {
	    throw new DomainException("error.LegalRegimen.no.beginDate");
	}
	if (endDate != null && endDate.isBefore(beginDate)) {
	    throw new DomainException("error.LegalRegimen.endDateBeforeBeginDate");
	}
    }       
}
