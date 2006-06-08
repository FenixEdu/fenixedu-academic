/*
 * Created on Sep 26, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain;

import org.joda.time.YearMonthDay;

public class Contract extends Contract_Base {

    public Contract() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public boolean belongsToPeriod(YearMonthDay beginDate, YearMonthDay endDate) {
        return (!this.getBeginDateYearMonthDay().isAfter(endDate)
                && (this.getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay().isBefore(beginDate)));            
    }

    public boolean isActive(YearMonthDay currentDate) {
        return (this.getEndDateYearMonthDay() == null || !this.getEndDateYearMonthDay().isBefore(currentDate));            
    }

    public void delete() {
        removeEmployee();
        removeMailingUnit();
        removeSalaryUnit();
        removeWorkingUnit();
        removeRootDomainObject();
        deleteDomainObject();
    }
}
