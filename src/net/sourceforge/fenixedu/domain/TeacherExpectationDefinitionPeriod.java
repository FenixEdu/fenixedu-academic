package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.util.DateFormatUtil;

public class TeacherExpectationDefinitionPeriod extends TeacherExpectationDefinitionPeriod_Base {

    private static final String DATE_ONLY_FORMAT = "yyyyMMdd";

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

        if ((DateFormatUtil.isAfter(DATE_ONLY_FORMAT, currentDate, this.getStartDate()) || (DateFormatUtil
                .equalDates(DATE_ONLY_FORMAT, currentDate, this.getStartDate())))
                && (DateFormatUtil.isBefore(DATE_ONLY_FORMAT, currentDate, this.getEndDate()) || DateFormatUtil
                        .equalDates(DATE_ONLY_FORMAT, currentDate, this.getEndDate()))) {
            return true;
        } else {
            return false;
        }

    }

}
