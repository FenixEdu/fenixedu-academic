/*
 * Created on Sep 26, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.DateFormatUtil;

public class Contract extends Contract_Base {

    public Contract() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public boolean belongsToPeriod(Date beginDate, Date endDate) {
        return (!this.getBeginDate().after(endDate)
                && (this.getEndDate() == null || !this.getEndDate().before(beginDate)));            
    }

    public boolean isActive(Date currentDate) {
        return (this.getEndDate() == null
                || (DateFormatUtil.equalDates("yyyyMMdd", this.getEndDate(), currentDate) || this
                        .getEndDate().after(currentDate)));            
    }

    public void delete() {
        removeEmployee();
        removeMailingUnit();
        removeSalaryUnit();
        removeWorkingUnit();
        deleteDomainObject();
    }
}
