/*
 * Created on Sep 26, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

public class Contract extends Contract_Base {

    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        if ((this.getBeginDate().after(beginDate) || this.getBeginDate().equals(beginDate))
                && (this.getEndDate() == null || (this.getEndDate().equals(endDate) || this.getEndDate().before(
                        endDate)))) {
            return true;
        }
        return false;
    }   
}
