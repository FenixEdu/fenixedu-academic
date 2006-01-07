package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

public class TeacherExpectationDefinitionPeriod extends TeacherExpectationDefinitionPeriod_Base {

    public TeacherExpectationDefinitionPeriod() {
        super();
    }

    public TeacherExpectationDefinitionPeriod(ExecutionYear executionYear, Date startDate, Date endDate) {
        super();
        this.setExecutionYear(executionYear);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    public void edit(Date startDate, Date endDate) {
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    public Boolean isPeriodOpen() {
        Date currentDate = Calendar.getInstance().getTime();

        if ((currentDate.compareTo(this.getStartDate()) >= 0)
                && (currentDate.compareTo(this.getEndDate()) <= 0)) {
            return true;
        } else {
            return false;
        }

    }

}
