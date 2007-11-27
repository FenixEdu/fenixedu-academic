package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class LeaveBean implements Serializable {

    private DomainReference<Leave> leave;

    private DateTime date;
    
    private YearMonthDay endYearMonthDay;
    
    public LeaveBean(Leave leave) {
	setLeave(leave);
	setDate(leave.getDate());
	setEndYearMonthDay(leave.getEndYearMonthDay());
    }
    
    public LeaveBean(Leave leave, Leave nextLeave) {
	setLeave(leave);
	setDate(leave.getDate());
	setEndYearMonthDay(nextLeave.getEndYearMonthDay());
    }
    
    public Leave getLeave() {
	return leave != null ? leave.getObject() : null;
    }

    public void setLeave(Leave leave) {
	this.leave = leave != null ? new DomainReference<Leave>(leave) : null;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public YearMonthDay getEndYearMonthDay() {
        return endYearMonthDay;
    }

    public void setEndYearMonthDay(YearMonthDay endYearMonthDay) {
        this.endYearMonthDay = endYearMonthDay;
    }
}
