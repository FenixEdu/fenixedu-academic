package net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTimeFieldType;

public class ChangeTutorshipByEntryYearBean implements Serializable {
    List<ChangeTutorshipBean> changeTutorshipsBeans;

    private DomainReference<ExecutionYear> executionYear;

    public ChangeTutorshipByEntryYearBean(ExecutionYear executionYear) {
	this.changeTutorshipsBeans = new ArrayList<ChangeTutorshipBean>();
	this.executionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    public ExecutionYear getExecutionYear() {
	return (executionYear == null ? null : executionYear.getObject());
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    public List<ChangeTutorshipBean> getChangeTutorshipsBeans() {
	return changeTutorshipsBeans;
    }

    public void setChangeTutorshipsBeans(List<ChangeTutorshipBean> changeTutorshipsBeans) {
	this.changeTutorshipsBeans = changeTutorshipsBeans;
    }

    public void addTutorship(Tutorship tutorship) {
	ChangeTutorshipBean bean = new ChangeTutorshipBean(tutorship);
	changeTutorshipsBeans.add(bean);
    }

    public class ChangeTutorshipBean implements Serializable {
	private DomainReference<Tutorship> tutorship;

	private Month tutorshipEndMonth;

	private Integer tutorshipEndYear;

	public ChangeTutorshipBean(Tutorship tutorship) {
	    setTutorship(tutorship);

	    if (tutorship.getEndDate() != null) {
		setTutorshipEndMonthFromTutorshipEndDate();
		setTutorshipEndYear(getTutorship().getEndDate().get(DateTimeFieldType.year()));
	    } else {
		setTutorshipEndMonth(null);
		setTutorshipEndYear(null);
	    }
	}

	public void setTutorshipEndMonthFromTutorshipEndDate() {
	    Month month = Month.values()[getTutorship().getEndDate().get(DateTimeFieldType.monthOfYear()) - 1];
	    setTutorshipEndMonth(month);
	}

	public Tutorship getTutorship() {
	    return (tutorship == null ? null : tutorship.getObject());
	}

	public void setTutorship(Tutorship tutorship) {
	    this.tutorship = new DomainReference<Tutorship>(tutorship);
	}

	public Month getTutorshipEndMonth() {
	    return tutorshipEndMonth;
	}

	public void setTutorshipEndMonth(Month tutorshipEndMonth) {
	    this.tutorshipEndMonth = tutorshipEndMonth;
	}

	public Integer getTutorshipEndYear() {
	    return tutorshipEndYear;
	}

	public void setTutorshipEndYear(Integer tutorshipEndYear) {
	    this.tutorshipEndYear = tutorshipEndYear;
	}
    }
}
