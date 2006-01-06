/*
 * Created on Dec 12, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

public class TeacherLegalRegimen extends TeacherLegalRegimen_Base {

    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        if (!this.getBeginDate().after(endDate)
                && (this.getEndDate() == null || !this.getEndDate().before(beginDate))) {
            return true;
        }
        return false;
    }
}
