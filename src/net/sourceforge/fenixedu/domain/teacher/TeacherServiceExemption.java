/*
 * Created on Nov 8, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.teacher;

import java.util.Date;

public class TeacherServiceExemption extends TeacherServiceExemption_Base {

    public void delete() {
        this.removeTeacher();
        super.deleteDomainObject();
    }

    public void edit(ServiceExemptionType type, Date beginDate, Date endDate) {
        this.setStart(beginDate);
        this.setEnd(endDate);
        this.setType(type);
    }

    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        if (this.getStart().before(endDate)
                && (this.getEnd() == null || ((this.getEnd().before(endDate) && this
                        .getEnd().after(beginDate))
                        || this.getEnd().after(endDate) || this.getEnd().equals(endDate)))) {
            return true;
        }
        return false;
    }
}
