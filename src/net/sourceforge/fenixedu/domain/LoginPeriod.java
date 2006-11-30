package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class LoginPeriod extends LoginPeriod_Base {

    public LoginPeriod(YearMonthDay begin, YearMonthDay end, Login login) {
	super();
	setBeginDate(begin);
	setEndDate(end);
	setLogin(login);
    }
    
    public LoginPeriod(YearMonthDay begin, Login login) {
	super();
	setBeginDate(begin);
	setLogin(login);
    }
    
    public void edit(YearMonthDay begin, YearMonthDay end) {
	setBeginDate(begin);
	setEndDate(end);
    }

    @Override
    public void setBeginDate(YearMonthDay beginDate) {
	if (beginDate == null) {
	    throw new DomainException("error.LoginPeriods.empty.beginDateTime");
	}
	super.setBeginDate(beginDate);
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
	if (getBeginDate() == null
		|| (endDate != null && !endDate.isAfter(getBeginDate()))) {
	    throw new DomainException("error.LoginPeriods.endDateBeforeBeginDate");
	}
	super.setEndDate(endDate);
    }

    @Override
    public void setLogin(Login login) {
	if (login == null) {
	    throw new DomainException("error.LoginPeriods.empty.login");
	}
	super.setLogin(login);
    }

    public void delete() {
	super.setLogin(null);
	removeRootDomainObject();
	super.deleteDomainObject();	
    }    
}
