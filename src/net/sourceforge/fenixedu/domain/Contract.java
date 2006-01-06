/*
 * Created on Sep 26, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.DateFormatUtil;

public class Contract extends Contract_Base {

    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        if (!this.getBeginDate().after(endDate)
                && (this.getEndDate() == null || !this.getEndDate().before(beginDate))) {
            return true;
        }
        return false;
    }

    public boolean isActive(Date currentDate) {
        if (this.getEndDate() == null
                || (DateFormatUtil.equalDates("yyyyMMdd", this.getEndDate(), currentDate) || this
                        .getEndDate().after(currentDate))) {
            return true;
        }
        return false;
    }

    public void delete() {
        removeEmployee();
        removeMailingUnit();
        removeSalaryUnit();
        removeWorkingUnit();
        deleteDomainObject();
    }
}
