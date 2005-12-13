/*
 * Created on Sep 26, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

public class Contract extends Contract_Base {

    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        if ((this.getBeginDate().before(beginDate) || this.getBeginDate().after(beginDate))
                && (this.getEndDate() == null || (this.getEndDate().before(endDate) || this
                        .getEndDate().after(endDate)))) {
            return true;
        }
        return false;
    }
    
    public void delete(){        
        this.removeEmployee();
        this.removeMailingUnit();
        this.removeSalaryUnit();
        this.removeWorkingUnit();
        super.deleteDomainObject();
    }
}
