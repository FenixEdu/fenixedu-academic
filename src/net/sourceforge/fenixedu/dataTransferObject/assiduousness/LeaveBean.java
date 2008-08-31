package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class LeaveBean implements Serializable {

    public static final Comparator<LeaveBean> COMPARATOR_BY_DATE = new Comparator<LeaveBean>() {

	@Override
	public int compare(LeaveBean o1, LeaveBean o2) {
	    return o1.getDate().compareTo(o2.getDate());
	}
	
    };

    private DomainReference<Leave> leave;

    private DateTime date;

    private LocalDate endLocalDate;

    public LeaveBean(Leave leave) {
	setLeave(leave);
	setDate(leave.getDate());
	setEndLocalDate(leave.getEndLocalDate());
    }

    public LeaveBean(Leave leave, Leave nextLeave) {
	setLeave(leave);
	setDate(leave.getDate());
	setEndLocalDate(nextLeave.getEndLocalDate());
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

    public LocalDate getEndLocalDate() {
	return endLocalDate;
    }

    public void setEndLocalDate(LocalDate endLocalDate) {
	this.endLocalDate = endLocalDate;
    }
}
