package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author Nuno & Joana
 */
public class InfoExecutionYear extends InfoObject {

    private final ExecutionYear executionYear;

    public InfoExecutionYear(final ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public String getYear() {
        return executionYear.getYear();
    }

    public boolean equals(Object obj) {
        if (obj instanceof InfoExecutionYear) {
            InfoExecutionYear infoExecutionYear = (InfoExecutionYear) obj;
            return getYear().equals(infoExecutionYear.getYear());
        }
        return false;
    }

    public String toString() {
        return executionYear.getYear();
    }

    public PeriodState getState() {
        return executionYear.getState();
    }

    public int compareTo(Object arg0) {
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;
        return this.getYear().compareTo(infoExecutionYear.getYear());
    }

    public Date getBeginDate() {
        return executionYear.getBeginDate();
    }

    public Date getEndDate() {
        return executionYear.getEndDate();
    }

    public static InfoExecutionYear newInfoFromDomain(final ExecutionYear executionYear) {
        return executionYear == null ? null : new InfoExecutionYear(executionYear);
    }

    public String getNextExecutionYearYear() {
        final int year1 = Integer.valueOf(getYear().substring(0, 4)).intValue() + 1;
        final int year2 = Integer.valueOf(getYear().substring(5, 9)).intValue() + 1;

        final StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append(year1).append('/').append(year2).toString();
    }

    public boolean after(InfoExecutionYear infoExecutionYear) {
        return getBeginDate().after(infoExecutionYear.getEndDate());
    }

    public InfoExecutionYear getNextInfoExecutionYear() {
        final ExecutionYear nextExecutionYear = executionYear.getNextExecutionYear();
        return nextExecutionYear == null ? null : InfoExecutionYear.newInfoFromDomain(nextExecutionYear);
    }
}
