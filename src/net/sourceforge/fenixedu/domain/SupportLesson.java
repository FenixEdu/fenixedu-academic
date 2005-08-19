package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.util.date.TimePeriod;

/**
 * @author Fernanda Quitério 17/10/2003
 * @author jpvl
 */
public class SupportLesson extends SupportLesson_Base implements ICreditsEventOriginator {

    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getProfessorship().getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

}
