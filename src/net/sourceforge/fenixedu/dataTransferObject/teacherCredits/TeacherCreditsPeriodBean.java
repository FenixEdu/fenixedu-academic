package net.sourceforge.fenixedu.dataTransferObject.teacherCredits;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.time.calendarStructure.TeacherCreditsFillingForDepartmentAdmOfficeCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.TeacherCreditsFillingForTeacherCE;

import org.joda.time.DateTime;

public class TeacherCreditsPeriodBean implements Serializable {

    private DateTime beginForTeacher;
    private DateTime endForTeacher;

    private DateTime beginForDepartmentAdmOffice;
    private DateTime endForDepartmentAdmOffice;

    private DomainReference<ExecutionSemester> executionPeriodReference;

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
	return executionPeriodReference != null ? executionPeriodReference.getObject() : null;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
	executionPeriodReference = executionSemester != null ? new DomainReference<ExecutionSemester>(executionSemester) : null;
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

	TeacherCreditsFillingForDepartmentAdmOfficeCE departmentAdmOffice = executionSemester
		.getTeacherCreditsFillingForDepartmentAdmOfficePeriod();
	setBeginForDepartmentAdmOffice(departmentAdmOffice != null ? departmentAdmOffice.getBegin() : null);
	setEndForDepartmentAdmOffice(departmentAdmOffice != null ? departmentAdmOffice.getEnd() : null);

	TeacherCreditsFillingForTeacherCE teacherCE = executionSemester.getTeacherCreditsFillingForTeacherPeriod();
	setBeginForTeacher(teacherCE != null ? teacherCE.getBegin() : null);
	setEndForTeacher(teacherCE != null ? teacherCE.getEnd() : null);
    }

    public boolean isTeacher() {
	return teacher;
    }

    public void setTeacher(boolean teacher) {
	this.teacher = teacher;
    }
}
