package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.TutorshipSummaryPeriod;

import org.joda.time.LocalDate;

public class TutorshipSummaryPeriodBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionSemester executionSemester;
    private LocalDate beginDate;
    private LocalDate endDate;

    public TutorshipSummaryPeriodBean() {
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;

        if (executionSemester != null && executionSemester.getTutorshipSummaryPeriod() != null) {
            setBeginDate(executionSemester.getTutorshipSummaryPeriod().getBeginDate());
            setEndDate(executionSemester.getTutorshipSummaryPeriod().getEndDate());
        }
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isValid() {
        return getExecutionSemester() != null && getBeginDate() != null && getEndDate() != null;
    }

    public void save() {
        if (getExecutionSemester().getTutorshipSummaryPeriod() != null) {
            getExecutionSemester().getTutorshipSummaryPeriod().update(this);
        } else {
            getExecutionSemester().setTutorshipSummaryPeriod(TutorshipSummaryPeriod.create(this));
        }
    }
}
