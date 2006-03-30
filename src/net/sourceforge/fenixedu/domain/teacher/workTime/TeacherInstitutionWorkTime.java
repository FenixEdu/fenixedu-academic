package net.sourceforge.fenixedu.domain.teacher.workTime;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.util.date.TimePeriod;

public class TeacherInstitutionWorkTime extends TeacherInstitutionWorkTime_Base implements ICreditsEventOriginator {

    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    public boolean belongsToExecutionPeriod(ExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
    }

    public void delete() {
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
