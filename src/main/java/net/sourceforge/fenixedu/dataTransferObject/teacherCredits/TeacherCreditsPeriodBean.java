package net.sourceforge.fenixedu.dataTransferObject.teacherCredits;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.credits.AnnualCreditsState;
import net.sourceforge.fenixedu.domain.time.calendarStructure.TeacherCreditsFillingForDepartmentAdmOfficeCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.TeacherCreditsFillingForTeacherCE;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;

public class TeacherCreditsPeriodBean implements Serializable {

    private DateTime beginForTeacher;
    private DateTime endForTeacher;

    private DateTime beginForDepartmentAdmOffice;
    private DateTime endForDepartmentAdmOffice;

    private ExecutionSemester executionPeriodReference;

    private AnnualCreditsState annualCreditsState;
    private DateTime sharedUnitCreditsBeginDate;
    private DateTime sharedUnitCreditsEndDate;
    private DateTime unitCreditsBeginDate;
    private DateTime unitCreditsEndDate;

    private boolean teacher;

    public TeacherCreditsPeriodBean(ExecutionSemester executionSemester) {
        setExecutionPeriod(executionSemester);
        refreshDates();
    }

    public TeacherCreditsPeriodBean(ExecutionSemester executionSemester, boolean teacher) {
        setExecutionPeriod(executionSemester);
        setTeacher(teacher);
        refreshDates();
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionPeriodReference;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        executionPeriodReference = executionSemester;
    }

    public DateTime getBeginForTeacher() {
        return beginForTeacher;
    }

    public void setBeginForTeacher(DateTime begin) {
        this.beginForTeacher = begin;
    }

    public DateTime getEndForTeacher() {
        return endForTeacher;
    }

    public void setEndForTeacher(DateTime end) {
        this.endForTeacher = end;
    }

    public DateTime getBeginForDepartmentAdmOffice() {
        return beginForDepartmentAdmOffice;
    }

    public void setBeginForDepartmentAdmOffice(DateTime beginForDepartmentAdmOffice) {
        this.beginForDepartmentAdmOffice = beginForDepartmentAdmOffice;
    }

    public DateTime getEndForDepartmentAdmOffice() {
        return endForDepartmentAdmOffice;
    }

    public void setEndForDepartmentAdmOffice(DateTime endForDepartmentAdmOffice) {
        this.endForDepartmentAdmOffice = endForDepartmentAdmOffice;
    }

    public void refreshDates() {

        ExecutionSemester executionSemester = getExecutionPeriod();

        TeacherCreditsFillingForDepartmentAdmOfficeCE departmentAdmOffice =
                executionSemester.getTeacherCreditsFillingForDepartmentAdmOfficePeriod();
        setBeginForDepartmentAdmOffice(departmentAdmOffice != null ? departmentAdmOffice.getBegin() : null);
        setEndForDepartmentAdmOffice(departmentAdmOffice != null ? departmentAdmOffice.getEnd() : null);

        TeacherCreditsFillingForTeacherCE teacherCE = executionSemester.getTeacherCreditsFillingForTeacherPeriod();
        setBeginForTeacher(teacherCE != null ? teacherCE.getBegin() : null);
        setEndForTeacher(teacherCE != null ? teacherCE.getEnd() : null);
        setAnnualCreditsState();
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }

    public AnnualCreditsState getAnnualCreditsState() {
        return annualCreditsState;
    }

    public void setAnnualCreditsState() {
        annualCreditsState = AnnualCreditsState.getAnnualCreditsState(getExecutionPeriod().getExecutionYear());
        setSharedUnitCreditsBeginDate(annualCreditsState.getSharedUnitCreditsInterval() != null ? annualCreditsState
                .getSharedUnitCreditsInterval().getStart() : null);
        setSharedUnitCreditsEndDate(annualCreditsState.getSharedUnitCreditsInterval() != null ? annualCreditsState
                .getSharedUnitCreditsInterval().getEnd() : null);
        setUnitCreditsBeginDate(annualCreditsState.getUnitCreditsInterval() != null ? annualCreditsState.getUnitCreditsInterval()
                .getStart() : null);
        setUnitCreditsEndDate(annualCreditsState.getUnitCreditsInterval() != null ? annualCreditsState.getUnitCreditsInterval()
                .getEnd() : null);
    }

    public DateTime getSharedUnitCreditsBeginDate() {
        return sharedUnitCreditsBeginDate;
    }

    public void setSharedUnitCreditsBeginDate(DateTime sharedUnitCreditsBeginDate) {
        this.sharedUnitCreditsBeginDate = sharedUnitCreditsBeginDate;
    }

    public DateTime getSharedUnitCreditsEndDate() {
        return sharedUnitCreditsEndDate;
    }

    public void setSharedUnitCreditsEndDate(DateTime sharedUnitCreditsEndDate) {
        this.sharedUnitCreditsEndDate = sharedUnitCreditsEndDate;
    }

    public DateTime getUnitCreditsBeginDate() {
        return unitCreditsBeginDate;
    }

    public void setUnitCreditsBeginDate(DateTime unitCreditsBeginDate) {
        this.unitCreditsBeginDate = unitCreditsBeginDate;
    }

    public DateTime getUnitCreditsEndDate() {
        return unitCreditsEndDate;
    }

    public void setUnitCreditsEndDate(DateTime unitCreditsEndDate) {
        this.unitCreditsEndDate = unitCreditsEndDate;
    }

    private Interval getSharedUnitCreditsInterval() {
        return new Interval(getSharedUnitCreditsBeginDate(), getSharedUnitCreditsEndDate());
    }

    private Interval getUnitCreditsInterval() {
        return new Interval(getUnitCreditsBeginDate(), getUnitCreditsEndDate());
    }

    @Atomic
    public void editIntervals() {
        annualCreditsState.setSharedUnitCreditsInterval(getSharedUnitCreditsInterval());
        annualCreditsState.setUnitCreditsInterval(getUnitCreditsInterval());
    }
}
