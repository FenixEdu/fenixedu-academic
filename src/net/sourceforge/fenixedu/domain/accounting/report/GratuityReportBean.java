package net.sourceforge.fenixedu.domain.accounting.report;

import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.joda.time.DateTime;

public class GratuityReportBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private ExecutionYear executionYear;

    private GratuityReportQueueJobType type;

    private DateTime beginDate;
    private DateTime endDate;

    public GratuityReportBean(final ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public DateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(DateTime beginDate) {
        this.beginDate = beginDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public GratuityReportQueueJobType getType() {
        return type;
    }

    public void setType(GratuityReportQueueJobType type) {
        this.type = type;
    }
}