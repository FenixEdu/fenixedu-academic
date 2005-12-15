/*
 * Created on Dec 12, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

public class TeacherLegalRegimen extends TeacherLegalRegimen_Base {

    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        if (this.getBeginDate().before(endDate)
                && (this.getEndDate() == null || ((this.getEndDate().before(endDate) && this
                        .getEndDate().after(beginDate))
                        || this.getEndDate().after(endDate) || this.getEndDate().equals(endDate)))) {
            return true;
        }
        return false;
    }
}
